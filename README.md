## Tier 1 client-server multithreaded JSON database application

Used technologies: **Java**, **JCommander**, **Gson**

**Features:**
- getting, deleting values from database JSON file
- setting values to existing and **non-existing keys!**
- modifying database by with usage of parameters or JSON file

**Request format**  
Examples of request parameters (only not nested objects):
   ```
 -t get -k key
 -t set -k key -v value 
 -t del -k key
 -t exit  
 ```
_Exit command is required to shut down the server_

Example of request with usage of JSON file:
```
-in fileName.json
```
Structure of JSON request file has to have following format:
```
{
  "type": "set",
  "key": ["person"],
  "value": "90"
}
```  
When reaching not-nested objects, this form is also acceptable:
``` 
{
  "type": "set",
  "key": "person",
  "value": "90"
}
``` 
For modification of nested objects, including those not existing objects, only array of keys is accepted:
``` 
{
  "type": "set",
  "key": [
    "person",
    "rocket",
    "launches"
  ],
  "value": "90"
}
``` 
Used JSON files should be places in /client/data folder
   




