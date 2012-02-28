/*
  AnalogReadSerial
 Reads an analog input on pin 0, prints the result to the serial monitor

 This example code is in the public domain.
 */
#include <GSM3HTTPClientService.h>
#include <GSM3ShieldV1.h>
char pars[60]="/notifyTemperature?temperature=";
char temp[5];

GSM3HTTPClientService httpcs;
GSM3ShieldV1 gsm(true);

void setup()
{
  Serial.begin(9600);

  gsm.begin();
  while(gsm.getCommandError()==0) delay(1000);

  //Modem GPRS attachment.
  gsm.attachGPRS("movistar.es", "movistar", "movistar", 1);
  while(gsm.getCommandError()==0) delay(1000);
  if (gsm.getCommandError()==1) Serial.println("Attached.");

}

void loop() {
  int sensorValue = analogRead(A0);
  Serial.println(sensorValue);
  // Button < 1029
  // light: check before
  //
  if(sensorValue < 240)
  {
    itoa(sensorValue, temp,10);
    strcat(pars, temp);
    strcat(pars, "&number=34678974636");
    Serial.println(pars);
    if(httpcs.httpGET("greenhackathon.appspot.com", 80, pars))
    {
      Serial.println("Response:");
      char c;
      while(c=httpcs.read())
        Serial.print(c);
      Serial.println("End Response");
    }
    else
    {
      Serial.println("Get Error");
    }
  }

  delay(1000);
}
