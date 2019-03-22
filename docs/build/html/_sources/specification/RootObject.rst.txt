DSM Object
==========

This is the root document object of the DSM document.

Fields:
    .. csv-table::
      :header: Field Name, Type, Description
      :stub-columns: 1
      :delim: |

      version | string | **REQUIRED**. This string MUST be the semantic version number of the DSM Specification version that the DSM document uses. The DSM field SHOULD be used by tooling specifications and clients to interpret the DSM document
      params_ | Map[string,any] | **params_** field is a map that contains parameter definition to configure DSM document and `Parsing Element`_s. 
      transformations_ | Map[string,`Transformation Element`_] | Deceleration of Map contains  transformationCode_ as key, and `Transformation Element`_ as value. `Transformation Element`_ holds lookup table to transform value from `Source Document`_ to destination document. 
      functions_ | Map[string,Function_] | Deceleration of Map contains function_ name  as key, and  `function deceleration <functions_>`_ as value.  functions are  used for custom parsing or calling services with parsed data. Functions implements FunctionExecutor interface.
      fragments_ | Map [String, `Parsing Element`_ ] |  A map contains declaration of reusable `Parsing Element`_. The fragment definition can be referenced with $ref_ field while defining `Parsing Element`_.
      result_ | `Parsing Element`_ | **REQUIRED**. The entry point of `Parsing Element`_ declarations. The result field defines structure of the output.  
      `$extends`_  | string | **`$extends`_** field is used for import external DSM document.






_`params`
----------

-------------------------


params_ field is a map that contains parameter definition to configure DSM document and `Parsing Element`_. The key of params map is string and case sensitive. The value of params map can be any type of json object(int, boolean, object, array) accepted by JSON and YAML specification. 

Example DSM document that contains params.



..  content-tabs::
     
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml

            version: 1.0
            params: 
                dateFormat: dd.MM.yyyy
                rootPath: fooBar/foo
                category: 
                    foo: bar
                acceptedCountryCode: [TR,US,FR]

      .. tab-container:: json
          :title: JSON  

          .. code-block:: json

             {
                 "version": 1.0,
                 "params":{
                       "dateFormat":"dd.MM.yyyy",
                       "rootPath":"fooBar/foo",
                       "category":{
                         "foo":"bar"
                       },
                       "acceptedCountryCode": ["TR","US","FR"]
                 }
             } 




_`transformations`
-------------------

-------------------------------


Deceleration of Map contains  transformationCode_ as key, and `Transformation Element`_ as value. `Transformation Element`_ holds lookup table to transform value from `Source Document`_ to destination document. 
 
Example CF document that contains transformations

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML
         
            .. code-block:: yaml
            
               version: 1.0
               transformations:
                   COUNTRY_CODE_TO_NAME:  
                       map:
                         DEFAULT: Other
                         TR: Turkey
                         US: United States
                             
      .. tab-container:: json
          :title: JSON
          
          .. code-block:: json
          
             {
          
                "version": 1.0,
                "transformations":{
                   "COUNTRY_CODE_TO_NAME":{
                    "map":{
                      "TR":"Turkey",
                      "US":"United States",
                      "DEFAULT":"Other"
                    }
                   }
                }
          
              } 
          


_`functions`
-------------

----------------------------

Deceleration of Map contains function_ name  as key, and  `function deceleration <functions_>`_ as value.  functions are  used for custom parsing or calling services with parsed data. Functions implements FunctionExecutor interface.
           
Example CF document that contains functions
 

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

          .. code-block:: yaml
          
             version: 1.0
             functions:
                insertProduct: com.example.InsertProduct
                approveOrder: com.example.ApproveOrder

      .. tab-container:: json
          :title: JSON
          
          .. code-block:: json
          
             {
          
                "version": 1.0,
                "functions":{
                   "insertProduct":"com.example.InsertProduct",
                   "approveOrder":"com.example.ApproveOrder"
                }
          
              } 


_`fragments`
-------------

----------------------------

A map contains declaration of reusable `Parsing Element`_. The fragment definition can be referenced with `$ref`_ field while defining `Parsing Element`_.
   
Example CF document that contains functions
 
..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML
          
         .. code-block:: yaml
         
            version: 1.0
            fragments:
               product: 
                 fields:
                    id: string
                    name: string           
                    price: double
                         
      .. tab-container:: json
          :title: JSON
           
           .. code-block:: json
            
              {
           
                 "version": 1.0,
                 "fragments":{
                    "product":{
                       "fields":{
                          "id":"string",
                          "name":"double",
                          "price":"double"
                       }            
                    }
                 }
               } 




_`result`
------------

-------------------------------

**REQUIRED**. The entry point of `Parsing Element`_ declarations. The result field defines structure of the output.  

Example CF document that contains result

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

          .. code-block:: yaml
          
             version: 1.0
             result: 
               type: object
               path: /
               fields:
                  id: string
                  name: string           
                  price: double

      .. tab-container:: json
         :title: JSON
           
         .. code-block:: json
          
             {
          
                "version": 1.0,
                "result":{
                   "type":"object",
                   "path":"/"         
                   "fields":{
                      "id":"string",
                      "name":"double",
                      "price":"double"
                    }
                }           
              } 



_`$extends`
---------------

-----------------------------

$extends field is used for import external DSM document to current DSM document. it's value is basically relative path  or URI definition of  external DSM document.   if it's value start with "$" sing, it is accepted as an expression_ and resolved by expression resolver. External DSM document will merged_ into current DSM document. $extends can also be list of path or URI.  Merge_ process start from first element to last element. Firstly current DSM document merged_ with first element then result of merge_ process extended to second element etc..
   
Example CF document that contains extends


..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            $extends: /foo/bar/external.yaml
            
         **or** 
         
         .. code-block:: yaml
         
            version: 1.0
            params:
               rootPath: /bar/foo/
            $extends: 
               - /foo/bar/external.yaml
               - $params.rootPath.concat("externalWithExpression.yaml") 

      .. tab-container:: json
          :title: JSON
          
          .. code-block:: json
          
             {
                "version": 1.0,
                "$extends": "/foo/bar/external.json"
              } 
              
          or    
          
          .. code-block:: json
          
             {
                "version": 1.0,
                "params":{
                "rootPath":"/bar/foo/"
                },
                "$extends": ["/foo/bar/external.json","$params.rootPath.concat('externalWithExpression.json')"]
              } 
