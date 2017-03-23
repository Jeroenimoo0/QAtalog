# QAtalog
Open source QA statistics tool in development.

Currently the project is not intended for use in production environment as it is relatively easy to overload the server. Use on your own risk.

# Developer Agreement & Policy
Some rules for the project to make sure updating the version of the project your are using does not bring any problems.

### Major version updates
- Functionality is allowed to become deprecated
- Deprecated functionality can be removed if it was deprecated in previous major releases

### Minor version updates
- Functionality is allowed to become deprecated
- Deprecated functionality will not be removed between minor releases

### Patch version updates
- No functionality will become deprecated

# License
This project falls under the GNU General Public License v3.0 which full license can be found in the [license.txt](license.txt) file.

# Documentation
Below is listed the documentation about the communication between the server and client. This will allow for custom client implementations on any platform.

The `Content-Type` of all request and responses are `application\json`

### Error handling
Every response will contain the following:
```
{
 "status":"<status>"
}
```

If the `status` is `error` two extra fields will be added:
- `cause` which is a short description of what was/went wrong
- `code` An error code used to identify the error

### Server status
URL: `http://<host>:8080/status` <br>
Type: `GET` <br>

Response:
```
{
 "status":"<status>"
}
```
`status` will be `ok` if the server is running correctly and will return `error` if something went wrong during the request.

### Creating users
URL: `http://<host>:8080/user/create/<did>` <br>
Type: `POST` <br>
Parameters: <br>
- `did` short for Device ID represents a unique ID for this device. <br>

Errors: <br>
- 0x0 - Missing Device ID parameter

Response:
```
{
 "status":"<status>",
 "uid":"<uid>"
}
```
`uid` short for User ID will be used for pushing data. The client should save this value ideally.

### Retrieving users
URL: `http://<host>:8080/user/<did>` <br>
Type: `GET` <br>
Parameters: <br>
- `did` short for Device ID represents a unique ID for this device. <br>

Errors: <br>
- 0x0 - Missing Device ID parameter
- 0x2 - No user with the given ID

Response:
```
{
 "status":"<status>",
 "uid":"<uid>"
}
```
`uid` short for User ID will be used for pushing data. The client should save this value ideally.
### Pushing data
URL: `http://<host>:8080/push/<uid>` <br>
Type: `POST` <br>
Parameters: <br>
- `uid` short for User ID represents a unique ID for this user. <br>

Payload example:
```
{
 "deaths":[
  {
   "x":10,
   "y":40,
   "cause":"FIRE"
  },
  {
   "x":60,
   "y":10,
   "cause":"POISON"
  }
  ],
  "wins":[
  {
   "x":10,
   "y":40,
   "time":4000
  }
  ]
}
```

Errors: <br>
- 0x0 - Missing User ID parameter

Response:
```
{
 "status":"<status>"
}
```
`status` will be `success` if the data was pushed successfully and `error` if something went wrong during the request.

# Server implementation
Current version: 1.0.0 Pre-release

# Projects
[Unreal engine 4](QAtalog-unreal4/README.md) (Simple)
* Generates user ID & stores it
* Simple instant push
* Can push any key/value data

[QAtalog webserver](QAtalog-webserver/)
* Standalone webserver
* Runs anywhere on java 8
* Builds using maven

[QAtalog client](QAtalog-client/) (Planned)
* C++ crossplatform
* Creating users
* Simple data batching
* Data pushing
