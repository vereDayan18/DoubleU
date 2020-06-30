#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>

// WiFi parameters
#define WLAN_SSID ""                    // Insert here your WiFi network name
#define WLAN_PASS ""               // Insert here the password for your WiFi network

#define buttonPin D5

// Functions declarations
HTTPClient http;

void setup() {
  Serial.begin(115200);
  delay(10);
  Serial.println();

  // Connect to WiFi
  Serial.print("Connecting to ");
  Serial.println(WLAN_SSID);
  WiFi.begin(WLAN_SSID, WLAN_PASS);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    delay(10);
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  pinMode(buttonPin, INPUT_PULLUP);
  pinMode(13, OUTPUT);

}

void loop()
{
  int sensorValue = digitalRead(buttonPin);
  if (sensorValue == LOW) {
    sendNotification();
  }
}

void sendNotification()
{
   if (WiFi.status() == WL_CONNECTED){                                //Check WiFi connection status 
   http.begin("http://double-u-server.herokuapp.com/postRequest");    //Specify request destination
   http.addHeader("Content-Type", "application/json");  //Specify content-type header
   String postMsg = String("{\"domain\" : \"Television\",\"name\" : \"Rina\",\"phoneNumber\" : \"0566778394\"}");
   int httpCode = http.POST(postMsg);                                 //Send the request
   String payload = http.getString();                                 //Get the response payload
   http.writeToStream(&Serial);
   Serial.println("httpCode is " + httpCode);                         //Print HTTP return code
   Serial.println("Payload is:" + payload);                           //Print request response payload
   http.end();                                                        //Close connection
 
 } else {
    Serial.println("Error in WiFi connection");   
 }
  delay(30000);  //Send a request every 30 seconds
}
