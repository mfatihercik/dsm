   
_`tagPath` and _`tagParentPath`
-------------------------------

The tagPath and tagParentPath fields indicate which tags are used in the `source document`_ 
during parsing. The value of those fields are regular expressions.

The **tagPath** field specifies the location of a tag in the `source document`_
relative to the tagPath field of the higher-level `Parsing Element`_ definition. 
The default value is the value in the fieldName_ field.

The **tagParentPath** field is used in a slightly more complex parsing definitions. 
it holds path to parent tag of the tag specified in the "tagPath" field.


The tagPath_ and tagParentPath_ fields can be defined as the relative path as in unix. 
Relative paths are resolved according to the structure in the DSM document, 
not by the structure in the source file.

Some example of relative path:

:current: ./

:parent: ../

:parentOfParent: ../../

:categoryInParent: ../category

:categoryInCurrent: ./category

To find the exact tag path for the current `Parsing Element`_, starting from the result field, 
all the *tagParentPath* and *tagPath* fields from top to bottom are merged with the "/" character. 

**tagParentAbsolutePath** and **tagAbsolutePath**  evaluated as follow:

         **currentObject** mean is `Parsing Element`_ that current tagPath and tagParentPath is defined
         **parentObject** mean is parent `Parsing Element`_ of currentObject      
         
         
         
         - parentObject = if (**tagParentPath+tagPath**) is relative path then find  parentObject by resolving relative path else currentObject.parent 

         - **tagParentAbsolutePath** = parentObject.tagPath+"/"+currentObject.tagParentPath      

         - **tagAbsolutePath** = absoluteTagParentPath+"/"+currentObject.tagPath
         

if **tagAbsolutePath**  regex match any  absolute_ path of tag, value of this tag evaluated by tagType_



      
To explain with example:

.. code-block:: yaml

   result:
      tagPath: /orders/order                              #tagPath = orders/order
                                                          #tagParentPath = ""  
                                                          #tagAbsolutePath= /orders/order
                                                          #tagParentAbsolutePath= /                                   
      fields:                       
         defaultName:                                     #tagPath =defaultName (default value is fieldName)
                                                          #tagParentPath = ""
                                                          #tagParentAbsolutePath =/orders/order (parent(result) absoluteTagPath))
                                                          #tagAbsolutePath =/orders/order/defaultName
         tagPathDefined:                       
            tagPath: status                               #tagPath =status
                                                          #tagParentPath = ""
                                                          #tagParentAbsolutePath =/orders/order (parent(result) absoluteTagPath))
                                                          #tagAbsolutePath =/orders/order/status
         tagPathAndTagParentPath:                    
               tagPath: name                              #tagPath = name
               tagParentPath: category                    #tagParentPath  = category
                                                          #tagParentAbsolutePath =/orders/order/category
                                                          #tagAbsolutePath =/orders/order/category/name
                     
         innerObject:             
              tagType: object             
              tagPath: foo                                #tagPath = "innerObject"   
                                                          #tagParentPath = ""
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/innerObject
              fields:     
                 normalPathInInnerObject:                 #tagPath = "normalPathInInnerObject"
                                                          #tagParentPath = ""
                                                          #tagParentAbsolutePath =/orders/order/innerObject
                                                          #tagAbsolutePath =/orders/order/innerObjcet/normalPathInInnerObject
                 relativeTagPath:            
                       tagPath:./defaultName              #tagPath = ../defaultName    (only level up)
                                                          #tagParentPath = ""
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/defaultName
                 relativeTagParentPath:            
                       tagPath:defaultName                #tagPath = defaultName    
                       tagParentPath:../                  #tagParentPath = "../" (only level up)
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/defaultName                                          relativeTagPathAndTagParentPath:                
                       tagPath:../defaultName             #tagPath = defaultName    
                       tagParentPath:../                  #tagParentPath = "../" (only level up)  
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/../defaultName (relative path of tagParentPath important. tagPath considered as regex)

                 relativePathFromResult:                
                   tagPath:/orders/order/defaultName      #tagPath = /orders/order/defaultName    
                                                          #tagParentPath = "" 
                                                          #tagParentAbsolutePath =/orders/order/defaultName    
                                                          #tagAbsolutePath =/orders/order     
  
  
  
  
      
**tagAbsolutePath and tagParentAbsolutePath**: 


.. csv-table::
    :header: fieldName, tagPath, tagParentPath,tagAbsolutePath, tagParentAbsolutePath
    :stub-columns: 1
    :align: left
    :delim: |   
    
      result | orders/order |  | order/simpleOrder | /
      result | orders/order |  | order/simpleOrder | /
      defaultName | defaultName(default value is fieldName) | order/simpleOrder/defaultName | order/simpleOrder
      tagPathDefined | status | order/simpleOrder/status | order/simpleOrder
      tagPathAndTagParentPath | status | order/simpleOrder/category/name | order/simpleOrder/category
      innerObject | innerObject | order/simpleOrder/innerObject | order/simpleOrder
      normalPathInInnerObject | normalPathInInnerObject | order/simpleOrder/innerObject/normalPathInInnerObject | order/simpleOrder/innerObject
      relativeTagPath | defaultName | order/simpleOrder/defaultName | order/simpleOrder
      relativeTagParentPath | defaultName | order/simpleOrder/defaultName | order/simpleOrder
      relativeTagPathAndTagParentPath | defaultName | order/simpleOrder/..defaultName (relative path of tagParentPath important. tagPath considered as regex) | order/simpleOrder
      relativePathFromResult | /orders/order/defaultName | order/simpleOrder/defaultName | order/simpleOrder
