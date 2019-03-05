_`tagType`
-------------

----------------------------------

tagType defines how tags in the source document are parsed. it also defines the structure of the output object and hierarchy of the object tree. it may need extra parameters. Extra parameters are provided with tagTypeParams_ field that extended_ to params_ field.
Basically, there are two main "tagType" categories which are "complex", and "simple". The complex category includes "tagTypes" which exposes complex data type such as object or arrays. the simple category includes tagTypes which expose data type in the type_ field.
     
     
Supported tagType's:
 
.. csv-table::
    :header: Type Name, Category,Java Type, Extra Parameters
    :stub-columns: 1
    :delim: |   
     
      std_  | Simple  | type_ | default tagType if not defined explicitly
      object_ | Complex | Map | 
      array_  | Complex |  List<Map> - List<type_> |
      sum_  | Simple | type_ | fields: array of fieldName of current object.
      multiply_  | Simple | type_ | **fields**: array of fieldName of current object.
      divide_  | Simple  | type_ | **fields**: array of fieldName of current object.
      join_  | Simple  | type_ | **fields**: array of fieldName of current object, **separator**: separator string. default is comma(,)      



_`std`
^^^^^^^^

------------------

std is basic tagType_ which copy the value of the tag in the source document to the current object. std is the default value of the tagType_ field.  Data type_ is defined in the type_ field. 


..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              tagType: object
              tagPath: /
              fields:
                 foo: string   # tag type is STD   
                 bar: int      # tag type is STD 
                 
      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "foo":"string",
                     "bar":"int"
                  }
               }
            }
   


_`object` 
^^^^^^^^^^^^

---------------

object tagType_ is used to expose an object. `Parsing Element`_ which has "object" tagType_ must have 'fields_' field.

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              tagType: object
              tagPath: /
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
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "id":"string",
                     "name":"string",
                     "price":"double"
                   }
               }
            } 


Above DSM document generate following output(values are only example) :

.. code-block:: json

   {
      "id":"11111",
      "name":"foo",
      "price":1111.111
   }
   

 

_`array`
^^^^^^^^^^^^

---------------------------

array tagType is used to expose an array. Items of the array may be a object or simple type_. if `Parsing Element`_  has "fields_" field then the array tagType_ exposes List<Object>.  if fields_ field is not defined, the data type of array item decided by type_ field.   
 

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              tagType: array  # EXPOSE [Object] array of Object
              tagPath: /
              fields:
                 id: string    
                 name: string  
                 price: double
                 tags:                 # EXPOSE [string] array of string
                   tagType: array
                   tagPath: tag
                   type: string

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/",         
                  "fields":{
                     "id":"string",
                     "name":"string",
                     "price":"double",
                     "tags":{
                        "tagType": "array",
                        "tagPath": "tag",
                        "type": "string"
                     }
                   }
               }
             } 


Above DSM document generate following output(values are only example) :

.. code-block:: json

   [{
      "id":"11111",
      "name":"foo",
      "price":1111.111,
      "tags":["foo","bar"]
   }]
   



_`sum`
^^^^^^^^^^^^^^

-----------------------------


*sum* tagType is used to sum  properties defined with "fields" in **tagTypeParams_**. if one of the properties that defined in *fields* does not exist in the current object,   it is accepted as ZERO.



if current property(`Parsing Element`_ that "*sum*" tagType_ is defined on) is defined in "fields" in **tagTypeParams_**,   current property value is added to total result.(sum with self)

**(Explained with example below)**

**tagTypeParams**: 
 
.. csv-table::
    :header: Name, Type,Description
    :stub-columns: 1
    :delim: |   
    
      fields | array | **REQUIRED** list of fieldName_ of the properties in parent `Parsing Element`_ to sum.
    

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              tagType: object
              tagPath: /
              fields:
                 foo: int   
                 bar: bar  
                 fooAndBar: 
                     tagPath: \.  # when current object closed
                     tagType: sum    # declare sum tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        fields:[foo,bar]  # sum foo,and bar fields then set to fooAndBar property of current object.
                 sumWithSelf: 
                     tagType: sum    # declare sum tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        fields:[foo,bar,sumWithSelf]  # sum foo, bar and sumWithSelf(current field) fields then set sumWithSelf to total property of current object. 

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "foo":"int",
                     "bar":"int",
                     "fooAndBar":{
                         "tagPath":"\.",
                         "tagType": "sum",
                         "tagTypeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "sumWithSelf":{ 
                        "tagType": "sum",
                        "type":"int",
                        "tagTypeParams":{
                           "fields":["foo","bar","sumWithSelf"]
                        }
                     }
                   }
               }
             } 





_`multiply`
^^^^^^^^^^^^^^

--------------------

*multiply* tagType is used to multiply  properties defined with "fields" in **tagTypeParams_**. if one of the properties that defined in *fields* does not exist in the current object,   it is accepted as ONE.


if current property(`Parsing Element`_ that "*multiply*" tagType_ is defined on) is defined in "fields" in **tagTypeParams_**,   current property value is multiplied with total result. (multiply with self)

**(Explained with example below)**

tagTypeParams_:

.. csv-table::
    :header: Name, Type,Description
    :stub-columns: 1
    :delim: |   
    
      fields | array | **REQUIRED** list of field name of the properties in **current object** to multiply.
    

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              tagType: object
              tagPath: /
              fields:
                 foo: int   
                 bar: int  
                 fooAndBar: 
                     tagPath: \.  # when current object closed
                     tagType: multiply    # declare multiply tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        fields:[foo,bar]  # multiply foo,and bar fields then set it to fooAndBar property of current object.
                 multiplyWithSelf: 
                     tagType: multiply    # declare multiply tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        fields:[foo,bar,multiplyWithSelf]  # multiply foo, bar and multiplyWithSelf(current field) fields then set it to multiplyWithSelf property of current object.       

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "foo":"int",
                     "bar":"int",
                     "fooAndBar":{
                         "tagPath":"\.",
                         "tagType": "multiply",
                         "tagTypeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "multiplyWithSelf":{ 
                        "tagType": "multiply"
                        "type":"int",
                        "tagTypeParams":{
                           "fields":["foo","bar","multiplyWithSelf"]
                        }
                     }
                   }
               }
            } 
    

    
_`divide`
^^^^^^^^^^^^^^

------------------------

*divide* tagType is used to divide  properties defined with "fields" in **tagTypeParams_**. if one of the properties that defined in *fields* does not exist in the current object,   it is accepted as ONE.


if current property(`Parsing Element`_ that "*divide*" tagType_ is defined on) is defined in "fields" in **tagTypeParams_**,   current property value is divided with total result. (divide with self)

**(Explained with example below)**
   

tagTypeParams_: 
 
.. csv-table::
    :header: Name, Type,Description
    :stub-columns: 1
    :delim: |   
    
      fields | array | **REQUIRED** list of field name of the properties in **current object** to divide.

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              tagType: object
              tagPath: /
              fields:
                 foo: int   
                 bar: int  
                 fooAndBar: 
                     tagPath: \.  # when current object closed
                     tagType: divide    # declare divide tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        fields:[foo,bar]  # divide foo with bar (foo/bar) fields then set it to fooAndBar property of current object.
                 divideWithSelf: 
                     tagType: divide    # declare divide tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        fields:[foo,bar,divideWithSelf]  # divide foo with bar then divide with divideWithSelf(current field) (foo/bar/divideWithSelf) fields then set it to sumWithSelf property of current object.       

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "foo":"int",
                     "bar":"int",
                     "fooAndBar":{
                         "tagPath":"\.",
                         "tagType": "divide",
                         "tagTypeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "divideWithSelf":{ 
                        "tagType": "divide",
                        "type":"int",
                        "tagTypeParams":{
                           "fields":["foo","bar","divideWithSelf"]
                        }
                     }
                   }
               }
            } 
    
    
    
_`join`
^^^^^^^^^^^^^^^^

-------------------------

*join* tagType is used to join properties defined with "fields" in **tagTypeParams_**. if one of the properties that defined in *fields* does not exist in the current object, it is skipped.  

if current property(`Parsing Element`_ that "*join*" tagType_ is defined on) is defined in 
"fields" in **tagTypeParams_**, current property value is included in to joining  (join with self)
Values are  separated by *separator* defined in  "tagTypeParams_". The default separator  is a comma(,)

tagTypeParams_: 
 
.. csv-table::
    :header: Name, Type,Description
    :stub-columns: 1
    :delim: |   
    
      fields | array | **REQUIRED** list of field name of the properties in **current object** to join.
      separator | string | separator string. default is comma(i)    
     

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              tagType: object
              tagPath: /
              fields:
                 foo: string   
                 bar: string  
                 fooAndBar: 
                     tagPath: \.  # when current object closed
                     tagType: join    # declare join tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        fields:[foo,bar]  # join foo and bar (foo,bar) fields then set it to fooAndBar property of current object.
                 joinWithSelf: 
                     tagType: join    # declare join tagType to sum foo and bar field
                     type:int
                     tagTypeParams:
                        separator: &
                        fields:[foo,bar,sumWithSelf]  # join foo,bar, and joinWithSelf(current field) (foo&bar&joinWithSelf) fields then set it to joinWithSelf property of current object.       

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "foo":"string",
                     "bar":"string",
                     "fooAndBar":{
                         "tagPath":"\.",
                         "tagType": "join",
                         "tagTypeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "joinWithSelf":{ 
                        "tagType": "join",
                        "type":"int",
                        "tagTypeParams":{
                           "separator": "&",
                           "fields":["foo","bar","joinWithSelf"]
                        }
                     }
                   }
               }
            } 



_`tagTypeParams`
----------------------

---------------------

tagTypeParams is used for passing extra parameters to tagType_ field. The tagTypeParams field is  extended_ to params_ field. 
   
Examples:

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result: 
              tagType: object
              tagPath: /
              fields:
                 foo: string   
                 bar: string  
                 fooAndBar: 
                     tagPath: \.  # when current object closed
                     tagType: join    # declare join tagType to concat foo and bar field
                     type:int
                     tagTypeParams:       # tagTypeParams is used to pass fields parameter to tagType
                        fields:[foo,bar] 

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"object",
                  "tagPath":"/"  ,       
                  "fields":{
                     "foo":"string",
                     "bar":"string",
                     "fooAndBar":{
                         "tagPath":"\.",
                         "tagType": "join",
                         "tagTypeParams":{
                              "fields":["foo","bar"]
                         }
                     }
                   }
               }
            } 
            