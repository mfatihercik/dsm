
*************
Introduction
*************

Declarative Stream Mapping(DSM) is a stream deserializer library that works for both XML and JSON. DSM allows you to make custom parsing, filtering, transforming, aggregating, grouping on any JSON or XML document at stream time(read once). There is no need to writing custom parser. DSM use yaml or json configuration file to parse data. Processed data can be deserialized to java classes.

*************
Definitions
*************


_`DSM Document`:
 A document (or set of documents) that defines or describes parsing  element definition uses and conforms to the DSM Specification.   
 
_`Source Document`:
   Document(File, Stream, String, HTTP Request Payload) contains JSON or XML data.
   


*************
Format
*************

DSM document is a JSON object, which maybe represented either in JSON or YAML format. 
All field names in the specification are case sensitive. This includes all fields that are used as keys in a map, except where explicitly noted that keys are case insensitive.

*******************
Document Structure
*******************

DSM document may be made up single document or  divided into multiple connected  parts at the discretion of the user. 
In later case, `$extends`_ fields must be used to reference those parts.  

.. include:: schema.rst
.. include:: AbsoluteTagPath.rst
.. include:: Expressions.rst
.. include:: MergeOfDocument.rst
.. include:: FieldAssignmentOrder.rst
