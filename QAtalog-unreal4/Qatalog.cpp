// Fill out your copyright notice in the Description page of Project Settings.

#include "Boomerang.h"
#include "Qatalog.h"

#include "Http.h"
#include "Json.h"


FString* UQatalog::UserID = new FString("Unkown");

void UQatalog::Init() {
	UE_LOG(LogTemp, Log, TEXT("Qatalog init..."));

	FString uid = FString();
	bool found = LoadUserID(uid);

	if (!found) RequestUserID();
	else {
		UE_LOG(LogTemp, Log, TEXT("Loaded existing UserID %s"), *uid);
		UserID = new FString(uid);
	}
}

bool UQatalog::SaveUserID(FString UserID)
{
	UE_LOG(LogTemp, Log, TEXT("Saving UserID %s"), *UserID);

	return FFileHelper::SaveStringToFile(UserID, *(FPaths::GameDir() + "userid.data"));
}

bool UQatalog::LoadUserID(FString& UserID)
{
	return FFileHelper::LoadFileToString(UserID, *(FPaths::GameDir() + "userid.data"));
}

bool UQatalog::RequestUserID()
{
	UE_LOG(LogTemp, Log, TEXT("Requesting UserID...."));

	FHttpModule * httpModule = &FHttpModule::Get();
	TSharedRef<IHttpRequest> HttpRequest = httpModule->CreateRequest();
	HttpRequest->SetVerb("POST");
	HttpRequest->SetURL(FString("http://localhost:8080/user/create/").Append(FPlatformMisc::GetMachineId().ToString()));
	HttpRequest->SetHeader("User-Agent", "QatalogClient/1.0");
	HttpRequest->SetHeader("Content-Type", "application/json");
	
	HttpRequest->OnProcessRequestComplete().BindLambda([](FHttpRequestPtr HttpRequest, FHttpResponsePtr HttpResponse, bool bSucceeded)
	{
		TSharedPtr<FJsonObject> JsonObject = MakeShareable(new FJsonObject());

		UE_LOG(LogTemp, Log, TEXT("Response: %s"), *HttpResponse->GetContentAsString());

		TSharedRef<TJsonReader<TCHAR>> JsonReader = TJsonReaderFactory<TCHAR>::Create(HttpResponse->GetContentAsString());

		FJsonSerializer::Deserialize(JsonReader, JsonObject);

		FString status = JsonObject->GetStringField("status");
		if (status.Equals("ok")) {
			FString uid = JsonObject->GetStringField("uid");

			UQatalog::UserID = new FString(uid);

			UE_LOG(LogTemp, Log, TEXT("Requested UserID is %s"), UQatalog::UserID);

			SaveUserID(uid);
		}
		else {
			int errorCode = JsonObject->GetIntegerField("code");
			UE_LOG(LogTemp, Log, TEXT("Error code %d"), errorCode);
		}
	});

	if (!HttpRequest->ProcessRequest())
	{
		UE_LOG(LogTemp, Warning, TEXT("Error request :("));
		return false;
	}

	return true;
}

bool UQatalog::PostData(FString key, TArray<FString> keys, TArray<FString> values)
{
	FHttpModule * httpModule = &FHttpModule::Get();
	TSharedRef<IHttpRequest> HttpRequest = httpModule->CreateRequest();
	HttpRequest->SetVerb("POST");
	HttpRequest->SetURL(FString("http://localhost:8080/push/").Append(*UserID));
	HttpRequest->SetHeader("User-Agent", "QatalogClient/1.0");
	HttpRequest->SetHeader("Content-Type", "application/json");
	
	FString content = "{\"";
	content.Append(key);
	content.Append("\":[{");

	int size = keys.Num();
	int commaSize = size - 1;
	for (int i = 0; i < size; i++) {
		content.Append("\"");
		content.Append(keys[i]);
		content.Append("\":");
		
		if (values[i].IsNumeric()) {
			content.Append(values[i]);
		}
		else {
			content.Append("\"");
			content.Append(values[i]);
			content.Append("\"");
		}

		if(i != commaSize) content.Append(",");
	}

	content.Append("}]}");

	HttpRequest->SetContentAsString(content);

	if (!HttpRequest->ProcessRequest())
	{
		UE_LOG(LogTemp, Warning, TEXT("Error request :("));
		return false;
	}

	return true;
}