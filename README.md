Simple (and fast) Google App-Engine API project to determine the city and the [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code based on caller's IP. May serve as a fallback for your Android applications if the GPS is unavailable.

![](https://github.com/renaudcerrato/ip2location/raw/master/art/screenshot.png)

# How it works? #

As a service to the app, App Engine adds the [following headers](https://cloud.google.com/appengine/docs/java/requests#Java_Request_headers) to all requests:

### X-AppEngine-Country

Country from which the request originated, as an ISO 3166-1 alpha-2 country code. App Engine determines this code from the client's IP address. Note that the country information is not derived from the WHOIS database; it's possible that an IP address with country information in the WHOIS database will not have country information in the X-AppEngine-Country header. Your application should handle the special country code ZZ (unknown country).

### X-AppEngine-Region 
Name of region from which the request originated. This value only makes sense in the context of the country in X-AppEngine-Country. For example, if the country is "US" and the region is "ca", that "ca" means "California", not Canada. The complete list of valid region values is found in the ISO-3166-2 standard.

### X-AppEngine-City 

Name of the city from which the request originated. For example, a request from the city of Mountain View might have the header value mountain view. There is no canonical list of valid values for this header.

### X-AppEngine-CityLatLong 

Latitude and longitude of the city from which the request originated. This string might look like "37.386051,-122.083851" for a request from Mountain View.


That RESTful API simply expose those headers and return them in JSON format: 

```
{
 "latitude": 40.714353,
 "longitude": -74.005973,
 "country": "US",
 "city": "new york",
 "region": "ny",
 "ip": "31.6.43.126",
}
```






