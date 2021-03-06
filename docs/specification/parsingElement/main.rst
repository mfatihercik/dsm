.. _`Parsing Element`:

_`Parsing Element Object`
=========================


Parsing Element is basic object of DSM. Parsing Element  contains set of rules for parsing specific tag of `Source Document`_  

Fields:
    .. csv-table::
          :header: Field Name, Type, Description
          :stub-columns: 1
          :delim: |
    
          fieldName_ | string | **REQUIRED**  fieldName_ define the name of the property to expose by current object. the fieldName is unique in object.
          dataType_ | string | **REQUIRED**. The data type of exposed field. it may have extra parameter provided with dataTypeParams_
          dataTypeParams_ | Map[string,any] | extra parameters for dataType field need to convert. for example. dateFormat for dataType
          type_ | string | the name of the parsing strategy for the current field. the default is STD. 
          typeParams_ | Map[string,any] | it is used for passing extra parameter to dataType_ converter. typeParams_ field extended_ to params_ field.
          path_ | string | The path field specifies the location of a tag in the `source document`_ relative to the path field of the higher-level Parsing Element definition. The default value is the value in the fieldName field.
          parentPath_ | string |The parentPath_ field is used in a slightly more complex parsing definitions. it holds path to parent tag of the tag specified in the "path_" field.
          default_ | string,`Default Object`_ | default value of the field if path_ not exist in the source document. if default value starts with "$" character it is accepted as expression_ and it is resolved by expression resolver.
          filter_ | string | The Filter field determines whether the value of a `Parsing Element Object`_ (complex or simple type_ does not matter) is added to the object tree. The filter field is an expression_ that returns true or false. 
          transformationCode_ | string | this field refers to the definition of the transformation_ to be used to transform the tag value. 
          function_ | string | name of the function in functions_ map. 
          normalize_ | string| this field is used to normalize the value of tag_. İt is an expression. 
          uniqueName_ | string | When "fieldName" fields of complex `Parsing Element`_ definitions are the same in the DSM document, these definitions are differentiated by using the "uniqueKey" field.
          xml_ |  Map[string,any] | XML related configuration goes under this tag.
          attribute_ | boolean | it is indicates that the current `Parsing Element Object`_ is an attribute on the tag pointed to by the parentPath field in the xml.
          overwriteByDefault_|  boolean | force using default_ field. Mostly used with filter field.
          fields_ | Map[string,String - Parsing Element Object - [Parsing Element Object] ] | fields  of the current object. its only valid for object and array type_
          `$ref`_ | string| $ref_ field is used to extends_ current config to given fragment_. it's value is an expression.



_`fieldName`
--------------

-------------------------------

**REQUIRED** The fieldName_ define the name of the property to expose by current object. the fieldName is unique in object.. However, a fieldName may have multiple `Parsing Element`. The fieldName is not explicitly defined. it is defined with fields_ property.  The keys of fields_ map are the fieldName of the Parsing Element. 
   
In blow DSM document, id, name, and price are fieldName_ of the result object. The result_ object exposes id, name and price property


..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML
          
         .. code-block:: yaml

            result:             # fieldName is result
            version: 1.0
              path: /
              type:object
                 id: string    # fieldName is id
              fields:
                 price: double
                 name: string  # fieldName is name

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

          



   
_`dataType`
----------

------------------------------

The dataType field defines data type (string, int, boolean etc.) of the exposed property. it is basicity a converter from string to given dataType type.
it may need extra parameters to convert a string to given data dataType. Extra parameters may be provided with params_ or dataTypeParams_. 
   
Supported dataType name and their corresponding java class:
   
.. csv-table::
    :header: Type Name, Java Type, Extra Parameters
    :stub-columns: 1
    :delim: |   
     
      int | int| 
      float |  float |
      short | short |
      double | double |
      long | long |
      _`date` | date | dateFormat(required)
      boolean | boolean |
      char | char |
      BigDecimal | BigDecimal |
      BigInteger | BigInteger |
      

.. _typeParamsExample:
   
..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            params: 
              dateFormat: dd.MM.yyyy  # default 'dateFormat' for all 'date' dataType
            result:             
              type:object
              path: /
              fields:
                 id: string    
                 name: string       # implicitly defined dataType. dataType is string
                 price: 
                    dataType: double    # explicitly defined dataType dataType. type is double
                 createDate: date   # implicitly defined the date dataType. and 'dateFormat' is defined in params field.
                 modifiedTime: 
                   dataType: date       # explicitly defined the date dataType. and 'dateFormat' is defined in dataTypeParams field.
                   dataTypeParams:
                      dateFormat: "yyyy-MM-dd'T'HH:mm:ss'Z'"

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "params":{
                  "dateFormat": "dd.MM.yyyy"
               },
               "result":{
                  "type":"object",
                  "path":"/",         
                  "fields":{
                     "id":"string",
                     "name":"double",
                     "price":{
                        "dataType": "double"
                     },
                     "createDate": "date",
                     "modifiedTime": {
                        "dataType": "date",
                        "dataTypeParams": {
                           "dateFormat":"yyyy-MM-dd'T'HH:mm:ss'Z'"
                        }
                     }
                   }
               }
             } 
    
_`dataTypeParams`
----------------

--------------------------------

dataTypeParams is used for passing extra parameters to a dataType_ convert.  dataTypeParams field extended_ to params_ field.
   
Check `example <typeParamsExample_>`_ here.
   
   
.. include:: parsingElement/tagTypeAndTagTypeParams.rst

.. include:: parsingElement/tagPathAndTagParentPath.rst

.. include:: parsingElement/fields.rst

.. include:: parsingElement/filter.rst
   
   
   
_`default`
------------------------

------------------

The *default* field holds the value to be assigned to a property  by default. The default value is assigned  when the path_  does not match the absolute_ path of any tag in the `source document`_.  
If the value of the default field is a string, 
this value is accepted as the value_ field of the `Default Object`_.

.. seealso::

   `Default Object`_

   Expression_

`assignment order`_ of the default  is from the bottom to up in an object.

Examples

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:             
              type: object
              path: /
              fields:
                 name:    
                     - filter: $ self.data.categoryType=='foo'
                       default: 
                          value: foo  # force set name to foo with filter
                          force: true
                     - path: name    
                 category: string          
                 productUnit:
                       default: $ self.data.categoryType=='foo'? 'LT': 'KG'    # default value is expression.  this default value is assigned after "categoryType" field assigned.
                 categoryType: 
                        default: "foo"   # default value a is a string. 

      .. tab-container:: json
         :title: JSON

          .. code-block:: json
          
             {
                "version": 1.0,
                "result":{
                   "type":"object",
                   "path":"/"  ,       
                   "fields":{
                      "name":"string",
                      "category":"string",            
                      "productUnit":{
                         "default": " $self.data.categoryType=='foo'? 'LT': 'KG'"
                      },
                      "categoryType":{
                         "default": "foo"
                      }
                    }
                }
              } 








_`transformationCode`
----------------------

----------

*transformationCode* field refers to the definition of the transformation_ to be used to transform the tag value. 
      
Below definition work as follows:
   
   - value of tag "/country_code"  is read from `source document`_
   - if this value exist in "COUNTRY_CODE_TO_NAME" transformation_ definition, get value that match.
   - if not exist, get "DEFAULT" value of "COUNTRY_CODE_TO_NAME" transformation_ definition
      
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
                      
             result:
               type: object
               path: /
               fields: 
                    country: 
                        path: country_code
                        transformationCode: COUNTRY_CODE_TO_NAME

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
               },
             "result":{
               "type": "object",
               "path": "/",
               "fields"{
                    "country":{
                        "path": "country_code"
                        "transformationCode": "COUNTRY_CODE_TO_NAME"
                    }
               }
             }
             } 

.. seealso::

   Transformations_


_`function`
-------------

-----------------

The *function* field refers to the definition of functions_ field to be used for the custom operation. 
For more detail about how functions_ works, look at functions_ sections.
   
Below definition work as follows:
   
   - all fields of product   are read from `source document`_
   -  When the "/product" tag is closed, the "com.example.InsertProduct" function in the "insertProduct" definition is called.
   
..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            functions:
               insertProduct: com.example.InsertProduct
               
             result:
               type: object
               path: /product
               function: insertProduct
               fields: 
                    name: string
                    price: long
                    image: string

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "functions":{
                  "insertProduct":"com.example.InsertProduct"
               },
               "result":{
                  "type": "object",
                  "path": "/",
                  "function": "insertProduct",
                  "fields": {
                       "name": "string",
                       "price": "long",
                       "image": "string"
                 }
               }
            } 
    
    
.. seealso::

   functions_


_`uniqueName`
-------------

----------------------

When "fieldName" fields of complex `Parsing Element`_ definitions are the same in the DSM document, 
these definitions are differentiated by using the "uniqueKey" field. 
This field is optional. The default value  is the value of the "fieldName" field. 
The uniqueName field may need in very complex document parsing.


Example Case:

In the following DSM document, both the users and the orders objects have a category field and the category field is an object. 
The uniqueName_ field is used to differentiate the category objects.


..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:
               type: object
               path: /
               fields: 
                    users:
                        type: array
                        fields:
                            name: string
                            email: string
                            category:
                                 type: object
                                 uniqueName: userCategory
                                 fields:
                                    categoryName: string                           
                    order:
                        type: object
                        fields:
                            id: string
                            category:
                              type: object
                              uniqueName: orderCategory
                              fields:
                                    categoryName: string
      .. tab-container:: json
         :title: JSON
         
         .. code-block:: json
         
            {
               "version": 1,
               "result": {
                  "type": "object",
                  "path": "/",
                  "fields": {
                     "users": {
                        "type": "array",
                        "fields": {
                           "name": "string",
                           "email": "string",
                           "category": {
                              "type": "object",
                              "uniqueName": "userCategory",
                              "fields": {
                                 "categoryName": "string"
                              }
                           }
                        }
                     },
                     "order": {
                        "type": "object",
                        "fields": {
                           "id": "string",
                           "category": {
                              "type": "object",
                              "uniqueName": "orderCategory",
                              "fields": {
                                 "categoryName": "string"
                              }
                           }
                        }
                     }
                  }
               }
            }


_`normalize`
--------------

-------------------

The normalize is used to normalize the value of the tag being read. 
Changes can be made to the raw string value of the tag by using normalize field. 
The value of this field is an expression. 

The following objects are available in Expression Context.


.. csv-table::
    :header: Name, Data Type, Description, Example
    :stub-columns: 1
    :delim: |  
    
    params_ | Map<string,any> | params_ object. | **params.dateFormat** =='dd.MM.yyyy' 
    self_ | Node_ | current node object that hold data of current complex type_ | **self.data.foo** => foo field of current node,  **self.parent.data.foo** => foo field of parent node, **self.data.bar.foo** => foo field of bar object in current node.
    all_ | Map<string,Node_> | A map that stores all nodes by the "uniqueName_" of `Parsing Element Object`_  | **all.bar.data.foo** => foo field of **bar** node,  **all.barList.data[0].foo** => *foo* field of first item of *barList* node
    value | string | raw string value of the current tag in `source document`_ | **value=='Computer'**,**value.startWith('bar')**

.. seealso::

   `Expression`_

_`xml`
--------------

-----------

The xml field is used to make extra definitions and to change "path" and "type" fields for XML format.

check `XML Object`_ for more detail

.. seealso::

   `XML Object`_
 


_`$ref`
---------

------------------

$ref field is used to extends_ `Parsing Element`_ to given fragments_. it's value is a Load Time Expression. 
fragments_ can be extends_ another fragments_ but can not extends itself.  
Sometimes we don't need parent properties. To exclude parent properties, define dataType_ as "exclude".  In example bellow category property is excluded.

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
               version: 1.0
               result: 
                 type: array
                 path: /
                 xml: 
                   path: "/Pets/Pet"
                 $ref: $fragments.pet
                 fields:
                     category: exclude     # import all properties of  fragments.pet except category property.
                     isPopular: 
                           default $self.data.tags.stream().anyMatch(s->s.name=='Popular')
               fragments:
                  tag:
                     type: object
                     fields:
                        id: int
                        name: string         
                  category:
                     type: object
                     fields:
                        id: int
                        name: string      
                  pet:
                    type: object
                    fields:
                        id: long
                        name: string
                        status: string         
                        category: 
                           $ref: $fragments.category  
                        photoUrls: 
                             type: array
                             path: photoUrls
                             xml: 
                                path: photoUrls/photoUrls
                        tags: 
                            type: array
                            path: tags
                            xml: 
                              path: tags/tag
                            $ref: $fragments.tag

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1,
               "result": {
                  "type": "array",
                  "path": "/",
                  "xml": {
                     "path": "/Pets/Pet"
                  },
                  "$ref": "$fragments.pet",
                  "fields": {
                     "category": "exclude",
                     "isPopular": "default $self.data.tags.stream().anyMatch(s->s.name=='Popular')"
                  }
               },
               "fragments": {
                  "tag": {
                     "type": "object",
                     "fields": {
                        "id": "int",
                        "name": "string"
                     }
                  },
                  "category": {
                     "type": "object",
                     "fields": {
                        "id": "int",
                        "name": "string"
                     }
                  },
                  "pet": {
                     "type": "object",
                     "fields": {
                        "id": "long",
                        "name": "string",
                        "status": "string",
                        "category": {
                           "$ref": "$fragments.category"
                        },
                        "photoUrls": {
                           "type": "array",
                           "path": "photoUrls",
                           "xml": {
                              "path": "photoUrls/photoUrls"
                           }
                        },
                        "tags": {
                           "type": "array",
                           "path": "tags",
                           "xml": {
                              "path": "tags/tag"
                           },
                           "$ref": "$fragments.tag"
                        }
                     }
                  }
               }
            }


