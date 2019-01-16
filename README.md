# App-Engine IP Geocoding Microservice

Simple (and fast) Google App-Engine application to fetch the city, coordinates, region ([ISO 3166-2](https://en.wikipedia.org/wiki/ISO_3166-2)) and the country code ([ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)) based on caller's IP address.

   * [Why ?](#why)
   * [Usage](#usage)
   * [Prerequisites](#prerequisites)
   * [Deployment](#deployment)
   * [How does it work?](#how-does-it-work)
   * [License](#license)
   
# Why?

The primary usage is to provide your application(s) a **fallback** if the geolocation is unavailable (or denied).

# Usage

The application exposes a simple endpoint:

```
$ curl https://<your-project-id>.appspot.com/api/v1/geocoder
{
 "lat": 40.714353,
 "long": -74.005973,
 "country": "US",
 "city": "new york",
 "region": "ny",
 "ip": "31.6.43.126",
}
```

# Prerequisites #

### Create a new Project
First of all, you'll need to go to your [Google Cloud console](https://console.cloud.google.com/projectselector/appengine/create?lang=java&st=true) to create a new App Engine application: 

![](https://i.imgur.com/WMVMHa3.png)


### Setup the Google Cloud SDK

Follow the [official documentation](https://cloud.google.com/sdk/docs/) to install the latest Google Cloud SDK. As a shorthand, you'll find below the Ubuntu/Debian instructions:


```bash
$ export CLOUD_SDK_REPO="cloud-sdk-$(lsb_release -c -s)"
$ echo "deb http://packages.cloud.google.com/apt $CLOUD_SDK_REPO main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
$ curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
$ sudo apt-get update && sudo apt-get install google-cloud-sdk
```

Do not forget to install the `app-engine-java` [component](https://cloud.google.com/sdk/docs/components#external_package_managers). If you installed the Google Cloud SDK using `apt-get`, it's as simple as:

```bash
$ sudo apt-get install google-cloud-sdk-app-engine-java
```

As a last step, configure the `gcloud` command line environment and select your newly created App Engine project when requested to do so:

```bash
$ gcloud init
$ gcloud auth application-default login
```
# Deployment

Clone (or [download](https://github.com/renaudcerrato/appengine-ip-to-location/archive/master.zip)) the source code, and deploy:

```bash
$ git clone https://github.com/renaudcerrato/appengine-ip-to-location.git
$ cd appengine-ip-to-location
$ ./gradlew appengineDeploy
```

# How does it work?

The application simply forwards App Engine [geolocation headers](https://cloud.google.com/appengine/docs/standard/java/reference/request-response-headers#app_engine-specific_headers) to the caller in a JSON format.

Here's the details about the headers returned:

### X-AppEngine-Country

Country from which the request originated, as an ISO 3166-1 alpha-2 country code. App Engine determines this code from the client's IP address. Note that the country information is not derived from the WHOIS database; it's possible that an IP address with country information in the WHOIS database will not have country information in the X-AppEngine-Country header. **Your application should handle the special country code ZZ (unknown country)**.

### X-AppEngine-Region 
Name of region from which the request originated. This value only makes sense in the context of the country in X-AppEngine-Country. For example, if the country is "US" and the region is "ca", that "ca" means "California", not Canada. The complete list of valid region values is found in the ISO-3166-2 standard.

### X-AppEngine-City 

Name of the city from which the request originated. For example, a request from the city of Mountain View might have the header value mountain view. There is no canonical list of valid values for this header.

### X-AppEngine-CityLatLong 

Latitude and longitude of the city from which the request originated. This string might look like "37.386051,-122.083851" for a request from Mountain View.

# License

```
Copyright 2018 Cerrato Renaud

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```





