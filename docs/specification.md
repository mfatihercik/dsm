

# Custom Format Transformer(CFT)
#### Version 0.1


## Intruduction

Custom Format Transformer (CFT) Tempalte, contains standards of parsing definition for XML or JSON formated data. Source document(Xml or JSON data) can transformed to any format by using CFT template. CFT, can make transformation, aggregation, grouping, filterig with high parformance on source document.


## Definitions

### <a name="parsingElement"></a>Parsing Element:
   A Map contains field name as a key and parsing configuration of the field as value.
   it is simplest and basic part of the specification. Basic example as follows.
 
**JSON** 
```json
   {"id": {"tagPath": "id","default": 1}}
 ```
   
**YAML**   
```yaml
   id: 
     tagPath: id
     default: 1 
 ```
   
   

### CFT Document:
 A document (or set of documents) that defines or describes parsing  element definition uses and conforms to the CFT Specification.   
 
### Source Document:
   Document(File, Stream, String, HTTP Request Payload) contains JSON or XML data.
   


## Specifications


### Format

CFT document is a JSON object, which maybe reperesented either in JSON or YAML format. 
All field names in the specification are case sensitive. This includes all fields that are used as keys in a map, except where explicitly noted that keys are case insensitive.

### Document Structure

CFT document may be made up single document or  dividen into multiple connected  parts at the discretion of the user. 
In later case, **$extends** fields must be used to reference those parts.  

### Schema

In the following description, if a field is not explicitly REQUIRED or described with a MUST or SHALL, it can be considered OPTIONAL.

#### <a name="cftObject"></a>CFT Object

This is the root document object of the CFT document.

##### Fields
 Field Name | Type | Description
---|:---:|---
cftVersion | string | REQUIRED. This string MUST be the semantic version number of the CFT Specification version that the CFT document uses. The cft field SHOULD be used by tooling specifications and clients to |interpret the OpenAPI document
[params](#params) | Map[string,any] | Default parameter map to configure both CFT document and CFT fields. it may contains default date format, rootPath etc.
sourceSystem | string | source system name of the CFT document generated from. This filed used with transformation
destinationSystem | string | target system name of the CFT document will transformed.  this field used with transfromation
transformations | Map[string,TransfromationElement]| Deceleration of Map contaions  [transfromationCode](#transformationCode) as key, and [TransformationElement](#transformationElement) as value. [TransformationElement](#transformationElement) holds lookup table to transform value from source document to destination system. 
functions | Map[string,Function] | Decleration of Map contains [fucntionName](#functionName) as key, and [function decleration](#function) as value.  
fragment | Map \[String,[ParsingElement](#parsingElement)\] |  A map contains decleration of reusable [ParsingElement](#parsingElement).
root | ParsingElement | **REQUEIRED**.  Entry point of ParsingElement decleration.
$extends  | string | **[$extends](#extends)** used to import external CFT document.



#### <a name="params"></a>params
**params** field a map that contains parameter definition to configure CFT document and ParsingElements. The key of params map is string and  case sensitive. value of params map can be any type of object(int, boolean, map, array) accepted by json and yaml specification. 

Example CFT document that contains params.
JSON
```json
{
   
   
   "cftVersion": 1.0,
   "params":{
          "dateFormat":"dd.MM.yyyy",
          "rootPath":"fooBar/foo",
          "category":{
            "foo":"bar"
          },
          "acceptedCountryCode": ["TR","US","FR"]
   }
   
 }
```
```yaml
cftVersion: 1.0
params: 
  dateFormat: dd.MM.yyyy
  rootPath: fooBar/foo
  category: 
     foo: bar
  acceptedCountryCode: [TR,US,FR]
```

#### <a name="sourceSystem"></a>sourceSystem
sourceSystem holds the name of the CFT document generated from. This filed used with transformation. we can declare same transfromationCode for different sourceSystem and destinationSystem



#### <a name="destinationSystem"></a>destinationSystem
destinationSystem holds target system name of the CFT document will transformed.
This filed used with transformation. we can declare same transfromationCode for different sourceSystem and destinationSystem
