_`type`
-------------

----------------------------------

type_ defines how tags in the source document are parsed. it also defines the structure of the output object and hierarchy of the object tree. it may need extra parameters. Extra parameters are provided with typeParams_ field that extended_ to params_ field.
Basically, there are two main "type" categories which are "complex", and "simple". The complex category includes "tagTypes" which exposes complex data dataType such as object or arrays. the simple category includes tagTypes which expose data type in the dataType_ field.
     
     
Supported type_'s:
 
.. csv-table::
    :header: Type Name, Category,Java Type, Extra Parameters
    :stub-columns: 1
    :delim: |   
     
      std_  | Simple  | dataType_ | default type_ if not defined explicitly
      object_ | Complex | Map | 
      array_  | Complex |  List<Map> - List<dataType_> |
      sum_  | Simple | dataType_ | fields: array of fieldName of current object.
      multiply_  | Simple | dataType_ | **fields**: array of fieldName of current object.
      divide_  | Simple  | dataType_ | **fields**: array of fieldName of current object.
      join_  | Simple  | dataType_ | **fields**: array of fieldName of current object, **separator**: separator string. default is comma(,)      



_`std`
^^^^^^^^

------------------

std is basic type_ which copy the value of the tag in the source document to the current object. std is the default value of the type_ field.  Data dataType_ is defined in dataType_ype_ field. 


..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              type: object
              path: /
              fields:
                 foo: string   # tag type is STD   
                 bar: int      # tag type is STD 
                 
      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type":"object",
                  "path":"/"  ,       
                  "fields":{
                     "foo":"string",
                     "bar":"int"
                  }
               }
            }
   


_`object` 
^^^^^^^^^^^^

---------------

object type_ is used to expose an object. `Parsing Element`_ which has "object" type_ must have 'fields_' field.

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
                  "path":"/"  ,       
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

array type_ is used to expose an array. Items of the array may be a object or simple dataType_. if `Parsing Element`_  has "fields_" field then the array type_ exposes List<Object>.  if fields_ field is not defined, the data type of array item decided dataType_ field.   
 

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              type: array  # EXPOSE [Object] array of Object
              path: /
              fields:
                 id: string    
                 name: string  
                 price: double
                 tags:                 # EXPOSE [string] array of string
                   type: array
                   path: tag
                   type: string

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type":"object",
                  "path":"/",         
                  "fields":{
                     "id":"string",
                     "name":"string",
                     "price":"double",
                     "tags":{
                        "type": "array",
                        "path": "tag",
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


*sum* type_ is used to sum  properties defined with "fields" in **typeParams_**. if one of the properties that defined in *fields* does not exist in the current object,   it is accepted as ZERO.



if current property(`Parsing Element`_ that "*sum*" type_ is defined on) is defined in "fields" in **typeParams_**,   current property value is added to total result.(sum with self)

**(Explained with example below)**

**typeParams**: 
 
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
              type: object
              path: /
              fields:
                 foo: int   
                 bar: bar  
                 fooAndBar: 
                     path: \.  # when current object closed
                     type: sum    # declare sum type to sum foo and bar field
                     type:int
                     typeParams:
                        fields:[foo,bar]  # sum foo,and bar fields then set to fooAndBar property of current object.
                 sumWithSelf: 
                     type: sum    # declare sum type to sum foo and bar field
                     type:int
                     typeParams:
                        fields:[foo,bar,sumWithSelf]  # sum foo, bar and sumWithSelf(current field) fields then set sumWithSelf to total property of current object. 

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type":"object",
                  "path":"/"  ,       
                  "fields":{
                     "foo":"int",
                     "bar":"int",
                     "fooAndBar":{
                         "path":"\.",
                         "type": "sum",
                         "typeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "sumWithSelf":{ 
                        "type": "sum",
                        "type":"int",
                        "typeParams":{
                           "fields":["foo","bar","sumWithSelf"]
                        }
                     }
                   }
               }
             } 





_`multiply`
^^^^^^^^^^^^^^

--------------------

*multiply* type_ is used to multiply  properties defined with "fields" in **typeParams_**. if one of the properties that defined in *fields* does not exist in the current object,   it is accepted as ONE.


if current property(`Parsing Element`_ that "*multiply*" type_ is defined on) is defined in "fields" in **typeParams_**,   current property value is multiplied with total result. (multiply with self)

**(Explained with example below)**

typeParams_:

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
              type: object
              path: /
              fields:
                 foo: int   
                 bar: int  
                 fooAndBar: 
                     path: \.  # when current object closed
                     type: multiply    # declare multiply type to sum foo and bar field
                     type:int
                     typeParams:
                        fields:[foo,bar]  # multiply foo,and bar fields then set it to fooAndBar property of current object.
                 multiplyWithSelf: 
                     type: multiply    # declare multiply type to sum foo and bar field
                     type:int
                     typeParams:
                        fields:[foo,bar,multiplyWithSelf]  # multiply foo, bar and multiplyWithSelf(current field) fields then set it to multiplyWithSelf property of current object.       

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type":"object",
                  "path":"/"  ,       
                  "fields":{
                     "foo":"int",
                     "bar":"int",
                     "fooAndBar":{
                         "path":"\.",
                         "type": "multiply",
                         "typeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "multiplyWithSelf":{ 
                        "type": "multiply"
                        "type":"int",
                        "typeParams":{
                           "fields":["foo","bar","multiplyWithSelf"]
                        }
                     }
                   }
               }
            } 
    

    
_`divide`
^^^^^^^^^^^^^^

------------------------

*divide* type_ is used to divide  properties defined with "fields" in **typeParams_**. if one of the properties that defined in *fields* does not exist in the current object,   it is accepted as ONE.


if current property(`Parsing Element`_ that "*divide*" type_ is defined on) is defined in "fields" in **typeParams_**,   current property value is divided with total result. (divide with self)

**(Explained with example below)**
   

typeParams_: 
 
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
              type: object
              path: /
              fields:
                 foo: int   
                 bar: int  
                 fooAndBar: 
                     path: \.  # when current object closed
                     type: divide    # declare divide type to sum foo and bar field
                     type:int
                     typeParams:
                        fields:[foo,bar]  # divide foo with bar (foo/bar) fields then set it to fooAndBar property of current object.
                 divideWithSelf: 
                     type: divide    # declare divide type to sum foo and bar field
                     type:int
                     typeParams:
                        fields:[foo,bar,divideWithSelf]  # divide foo with bar then divide with divideWithSelf(current field) (foo/bar/divideWithSelf) fields then set it to sumWithSelf property of current object.       

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type":"object",
                  "path":"/"  ,       
                  "fields":{
                     "foo":"int",
                     "bar":"int",
                     "fooAndBar":{
                         "path":"\.",
                         "type": "divide",
                         "typeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "divideWithSelf":{ 
                        "type": "divide",
                        "type":"int",
                        "typeParams":{
                           "fields":["foo","bar","divideWithSelf"]
                        }
                     }
                   }
               }
            } 
    
    
    
_`join`
^^^^^^^^^^^^^^^^

-------------------------

*join* type_ is used to join properties defined with "fields" in **typeParams_**. if one of the properties that defined in *fields* does not exist in the current object, it is skipped.  

if current property(`Parsing Element`_ that "*join*" type_ is defined on) is defined in 
"fields" in **typeParams_**, current property value is included in to joining  (join with self)
Values are  separated by *separator* defined in  "typeParams_". The default separator  is a comma(,)

typeParams_: 
 
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
              type: object
              path: /
              fields:
                 foo: string   
                 bar: string  
                 fooAndBar: 
                     path: \.  # when current object closed
                     type: join    # declare join type to sum foo and bar field
                     type:int
                     typeParams:
                        fields:[foo,bar]  # join foo and bar (foo,bar) fields then set it to fooAndBar property of current object.
                 joinWithSelf: 
                     type: join    # declare join type to sum foo and bar field
                     type:int
                     typeParams:
                        separator: &
                        fields:[foo,bar,sumWithSelf]  # join foo,bar, and joinWithSelf(current field) (foo&bar&joinWithSelf) fields then set it to joinWithSelf property of current object.       

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type":"object",
                  "path":"/"  ,       
                  "fields":{
                     "foo":"string",
                     "bar":"string",
                     "fooAndBar":{
                         "path":"\.",
                         "type": "join",
                         "typeParams":{
                              "fields":["foo","bar"]
                         }
                     },
                     "joinWithSelf":{ 
                        "type": "join",
                        "type":"int",
                        "typeParams":{
                           "separator": "&",
                           "fields":["foo","bar","joinWithSelf"]
                        }
                     }
                   }
               }
            } 



_`typeParams`
----------------------

---------------------

typeParams is used for passing extra parameters to type_ field. The typeParams field is  extended_ to params_ field. 
   
Examples:

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result: 
              type: object
              path: /
              fields:
                 foo: string   
                 bar: string  
                 fooAndBar: 
                     path: \.  # when current object closed
                     type: join    # declare join type to concat foo and bar field
                     type:int
                     typeParams:       # typeParams is used to pass fields parameter to type
                        fields:[foo,bar] 

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "type":"object",
                  "path":"/"  ,       
                  "fields":{
                     "foo":"string",
                     "bar":"string",
                     "fooAndBar":{
                         "path":"\.",
                         "type": "join",
                         "typeParams":{
                              "fields":["foo","bar"]
                         }
                     }
                   }
               }
            } 
            