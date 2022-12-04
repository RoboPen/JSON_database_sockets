## Tier 1 client-server multithreaded architecture

Used technologies: **Java**, **JCommander**, **Gson**

**Features:**
- getting, deleting values from database JSON file
- setting values to existing and **non-existing keys!**  
- modifying database by CLI with usage of parameters or JSON file

**Request format**  
Examples of parameters requests:
   ```
 -t get -k key
 -t set -k key -v value 
 -t del -k key
 -t exit  
 ```
_Exit command is required to shut down the server_  

Example of request with usage of JSON file:
```
-in name.json
```
Structure of JSON file has to have following keys:
```
{
  "type": "set",
  "key": ["person"],
  "value": "90"
}
```  
When reaching not-nested key, this form is also acceptable:
``` 
{
  "type": "set",
  "key": "person",
  "value": "90"
}
``` 
For modification of nested keys, including those not existing keys, only array of keys is accepted:
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
   




