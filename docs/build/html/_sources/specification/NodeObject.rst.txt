.. _node:
.. _nodes:

Node Object
============
The node is a data structure used in the DSM to store data and create the hierarchy of the DSM document.
Nodes are created with the complex tagType_ definition during parsing of source document. 
It can be used in the parsing time expressions
    
Fields:

.. csv-table::
      :header: Field Name, Type, Description
      :stub-columns: 1
      :delim: |
      
      parent | Node |  parent node.
      data  | Map<String,Object>, List<Object> | holds the value of the current node.
      
    
   
parent
-------

  parent field holds parent of the current node. parent of the result node is null.  
  
  
  Example usages:
  
  :self: point to current node
  :self.parent: point to parent of current node.
  :self.parent.parent: point to parent of current node.
  :self.parent==null: is current node result node.
  
  
data
-----

  The data field holds the value of the current node. 
  
  if tagType_ definition  of `Parsing Element`_ is an object_, data is a Map that contains properties of the current  `Parsing Element`_ definitions.
  
  Example Usage:
  
  :self.data: data is map
  :self.data.foo: foo property of current node
  :self.parent.data.foo: foo property of parent node
  :self.data.bar.foo: foo property of the bar object in current node
  
If the tagType_ definition of the current `Parsing Element` is an array:
 
and If the parsing element definition has fields, data of one is  Array , data of other is Map , two node is created.  
parent node holds array,  child node holds map.

self.data is a Map
self.parent.data is Array.

Example Usage:
  
  :self.data: data is map
  :self.data.foo: foo property of current node
  :self.parent[0].data.foo: foo property of first element of current array
  :self.parent[self.parent.size()-1].bar: last element of current array
  


and If there are no fields_ in the `Parsing Element`_ definition,a node  which the data field is array is created. 

self.data is Array


Example Usage:
  
  :self.data: data is array
  :self.data[0]: first property of array
  :self.parent.data.foo: foo property of parent node
  :self.parent[self.parent.size()-1].bar: last element of current array
