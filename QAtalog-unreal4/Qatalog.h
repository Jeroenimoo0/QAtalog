// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "Kismet/BlueprintFunctionLibrary.h"
#include "Qatalog.generated.h"

/**
 * 
 */
UCLASS()
class BOOMERANG_API UQatalog : public UBlueprintFunctionLibrary
{
	GENERATED_BODY()

public:
	UFUNCTION(BlueprintCallable, Category = "Qatalog")
	static void Init();

	UFUNCTION(BlueprintCallable, Category = "Qatalog")
	static bool PostData(FString key, TArray<FString> keys, TArray<FString> values);
protected:
	UFUNCTION(BlueprintCallable, Category = "Qatalog")
	static bool SaveUserID(FString UserID);

	UFUNCTION(BlueprintPure, Category = "Qatalog")
	static bool LoadUserID(FString& UserID);

	UFUNCTION(BlueprintCallable, Category = "Qatalog")
	static bool RequestUserID();

	static FString* UserID;
};
