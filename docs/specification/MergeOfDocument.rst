
.. _merge:
.. _merged:
.. _extended:
.. _extend:
.. _extends:

*************************
Merge of DSM Document
*************************

---------------------


`DSM document`_ can extends to another DSM document by using `$extends`_ field.

`Parsing Element`_ can extends to another Parsing Element by using `$ref`_ field.

DSM documents and Parsing Elements are merged with each other.

Before going to explain merge process lets make some definition.

source:
  DSM Document or Parsing Element that we want to extend to another.

target:
  DSM Document or Parsing Element that we want to extend to.


Merge process work as follows:
   for every field of target do followings:   
       1. if field **not exist** in source, **copy** value of target to source.
       
       2. if field **exist** in source do followings     
       
          2.1.  if  **type** of fields  is **different** then **skip** this field.
             
          2.2.  if  **type** of fields  is **same** then do followings          
             
            2.2.1.  if **type** is **simple type** (string,number) then **skip** this field (do not copy target to source)
                 
            2.2.2.  if **type** is **array** then **add** target values to **start of** the source values
                 
            2.2.3.  if **type** is **map** then **start Merge process** for those two map.
             
Example  merge process of DSM documents:

external DSM Document:(external.yaml)

.. code-block:: yaml

   version: 1.0
   params:
     dateFormat: dd.MM.yyyy
     rootPath: /foo/bar
     acceptedCountryCode: [TR,US,FR]
   transformations:
       COUNTRY_CODE_TO_NAME: 
           map:
             DEFAULT: Other
             TR: Turkey
             US: United States       
   result: 
     fields:
        id: string
        name: string           
        price: double


current DSM Document:

.. code-block:: yaml

   version: 1.0
   $extends: $params.rootPath.concat("external.yaml") # resolve expression 
   params:
     rootPath: /DSM/MAIN
     acceptedCountryCode: [UK]
   transformations:
       COUNTRY_CODE_TO_NAME:  
           map:
             UK: United Kingdom
   result: 
     tagType: object
     tagPath: /
     fields:
        category: string
        
        
After merge process following configuration will take effect:


.. code-block:: yaml

   version: 1.0
   $extends: $params.rootPath.concat("external.yaml") 
   params:
     dateFormat: dd.MM.yyyy # (rule 1) imported from external document 
     rootPath: /DSM/MAIN  # (rule 2.2.1)  overwritten by current DSM document
     acceptedCountryCode: [TR,US,FR,UK] #(rule 2.2.2)  external list element added to start off current list element(UK is only exist in current document and located at the end ) 
   transformations:    #(rule 2.2.3)  transformations field exist in both source and target and type is MAP
       COUNTRY_CODE_TO_NAME:   #(rule 2.2.3)
           map:
             UK: United Kingdom  # only exist in current DSM document
             DEFAULT: Other     # (rule 1) imported from external document
             TR: Turkey     # (rule 1) imported from external document
             US: United States  # (rule 1) imported from external document
             
   result: 
     tagType: object  # exist only current DSM document
     tagPath: /
     fields:
        category: string  # exist only current DSM document
        id: string        
        name: string      # imported from external document     
        price: double    
        
