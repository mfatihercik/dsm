
_`XML Object`
==============

---------------

XML Object is used to make extra definitions and to change "path_" and "parentPath_" fields for xml format 

Fields:
    .. csv-table::
      :header: Field Name, Type, Description
      :stub-columns: 1
      :delim: |
      
      path_ | string | xml specific path_ definition default value is path_ field of `Parsing Element Object`_
      parentPath_ | string | xml specific parentPath_ definition. default value is parentPath_ field of `Parsing Element Object`_
      attribute_ | boolean |  attribute field is indicates that the current `Parsing Element Object`_ is an attribute on the tag pointed to by the parentPath field in the xml.
      
      

_`attribute`
------------

The attribute field is indicates that the current `Parsing Element`_ is an attribute on the tag pointed to by the parentPath_ field in the xml.
    
    
Examples:
   
..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:
               type: object
               path: / 
               xml:
                  path: /Pets/Pet    # xml specific path definition
               fields: 
                    id:
                       dataType: long
                       xml:
                         attribute: true   # id field is an attribute that is located at /Pets/Pet tag. 
                    name: string
                    price: long
                    image: string

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
         
               "version": 1.0,      
               "result":{
                  "type": "object",
                  "path": "/" ,
                  "xml":{
                     "path": "/Pets/Pet"
                   }
                  "fields": {
                       "id": {
                           "dataType": "long",
                           "xml":{
                              "attribute": "true"
                           }
                       },              
                       "name": "string",
                       "price": "long",
                       "image": "string"
                 }
               }
            } 

.. seealso::

   xml_