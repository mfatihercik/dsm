
_`XML Object`
==============

---------------

XML Object is used to make extra definitions and to change "tagPath_" and "tagParentPath_" fields for xml format 

Fields:
    .. csv-table::
      :header: Field Name, Type, Description
      :stub-columns: 1
      :delim: |
      
      tagPath_ | string | xml specific tagPath_ definition default value is tagPath_ field of `Parsing Element Object`_
      tagParentPath_ | string | xml specific tagParentPath_ definition. default value is tagParentPath_ field of `Parsing Element Object`_
      attribute_ | boolean |  attribute field is indicates that the current `Parsing Element Object`_ is an attribute on the tag pointed to by the tagParentPath field in the xml.
      
      

_`attribute`
------------

The attribute field is indicates that the current `Parsing Element`_ is an attribute on the tag pointed to by the tagParentPath_ field in the xml.
    
    
Examples:
   
..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
            version: 1.0
            result:
               tagType: object
               tagPath: / 
               xml:
                  tagPath: /Pets/Pet    # xml specific tagPath definition
               fields: 
                    id:
                       type: long
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
                  "tagType": "object",
                  "tagPath": "/" ,
                  "xml":{
                     "tagPath": "/Pets/Pet"
                   }
                  "fields": {
                       "id": {
                           "type": "long",
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