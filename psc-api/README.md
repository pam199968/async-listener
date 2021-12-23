# psc-api

psc-api-maj-v2
- API version: 1.0
  - Build date: 2021-12-08T16:04:23.441Z[GMT]

API CRUD for Personnels et Structures de santé


*Automatically generated by the [Swagger Codegen](https://github.com/swagger-api/swagger-codegen)*


## Requirements

Building the API client library requires:
1. Java 1.7+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>fr.ans.psc</groupId>
  <artifactId>psc-api</artifactId>
  <version>1.0.0</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "fr.ans.psc:psc-api:1.0.0"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/psc-api-1.0.0.jar`
* `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java
import fr.ans.psc.*;
import fr.ans.psc.auth.*;
import fr.ans.psc.model.*;
import fr.ans.psc.api.PsApi;

import java.io.File;
import java.util.*;

public class PsApiExample {

    public static void main(String[] args) {
        
        PsApi apiInstance = new PsApi();
        Ps body = new Ps(); // Ps | The Ps to be created
        try {
            apiInstance.createNewPs(body);
        } catch (ApiException e) {
            System.err.println("Exception when calling PsApi#createNewPs");
            e.printStackTrace();
        }
    }
}
import fr.ans.psc.*;
import fr.ans.psc.auth.*;
import fr.ans.psc.model.*;
import fr.ans.psc.api.PsApi;

import java.io.File;
import java.util.*;

public class PsApiExample {

    public static void main(String[] args) {
        
        PsApi apiInstance = new PsApi();
        String psId = "psId_example"; // String | 
        try {
            apiInstance.deletePsById(psId);
        } catch (ApiException e) {
            System.err.println("Exception when calling PsApi#deletePsById");
            e.printStackTrace();
        }
    }
}
import fr.ans.psc.*;
import fr.ans.psc.auth.*;
import fr.ans.psc.model.*;
import fr.ans.psc.api.PsApi;

import java.io.File;
import java.util.*;

public class PsApiExample {

    public static void main(String[] args) {
        
        PsApi apiInstance = new PsApi();
        String psId = "psId_example"; // String | 
        try {
            apiInstance.forceDeletePsById(psId);
        } catch (ApiException e) {
            System.err.println("Exception when calling PsApi#forceDeletePsById");
            e.printStackTrace();
        }
    }
}
import fr.ans.psc.*;
import fr.ans.psc.auth.*;
import fr.ans.psc.model.*;
import fr.ans.psc.api.PsApi;

import java.io.File;
import java.util.*;

public class PsApiExample {

    public static void main(String[] args) {
        
        PsApi apiInstance = new PsApi();
        String psId = "psId_example"; // String | 
        try {
            Ps result = apiInstance.getPsById(psId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PsApi#getPsById");
            e.printStackTrace();
        }
    }
}
import fr.ans.psc.*;
import fr.ans.psc.auth.*;
import fr.ans.psc.model.*;
import fr.ans.psc.api.PsApi;

import java.io.File;
import java.util.*;

public class PsApiExample {

    public static void main(String[] args) {
        
        PsApi apiInstance = new PsApi();
        Ps body = new Ps(); // Ps | 
        try {
            apiInstance.updatePs(body);
        } catch (ApiException e) {
            System.err.println("Exception when calling PsApi#updatePs");
            e.printStackTrace();
        }
    }
}
```

## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080/api/v1*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*PsApi* | [**createNewPs**](docs/PsApi.md#createNewPs) | **POST** /ps | Create new Ps
*PsApi* | [**deletePsById**](docs/PsApi.md#deletePsById) | **DELETE** /ps/{psId} | Delete Ps by id
*PsApi* | [**forceDeletePsById**](docs/PsApi.md#forceDeletePsById) | **DELETE** /ps/force/{psId} | Physical delete of Ps
*PsApi* | [**getPsById**](docs/PsApi.md#getPsById) | **GET** /ps/{psId} | Get Ps by id
*PsApi* | [**updatePs**](docs/PsApi.md#updatePs) | **PUT** /ps | Update Ps
*StructureApi* | [**createNewStructure**](docs/StructureApi.md#createNewStructure) | **POST** /structure | Create new structure
*StructureApi* | [**deleteStructureByStructureId**](docs/StructureApi.md#deleteStructureByStructureId) | **DELETE** /structure/{structureId} | Delete structure by id
*StructureApi* | [**getStructureById**](docs/StructureApi.md#getStructureById) | **GET** /structure/{structureId} | Get structure by id
*StructureApi* | [**updateStructure**](docs/StructureApi.md#updateStructure) | **PUT** /structure | Update structure
*ToggleApi* | [**togglePsref**](docs/ToggleApi.md#togglePsref) | **PUT** /toggle | toggle PsRef mapping

## Documentation for Models

 - [Error](docs/Error.md)
 - [Expertise](docs/Expertise.md)
 - [Profession](docs/Profession.md)
 - [Ps](docs/Ps.md)
 - [PsRef](docs/PsRef.md)
 - [Structure](docs/Structure.md)
 - [StructureRef](docs/StructureRef.md)
 - [WorkSituation](docs/WorkSituation.md)

## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

superviseurs.psc@esante.gouv.fr