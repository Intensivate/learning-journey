Function **definitions** start with `def`.

Function **name** follows.

Function **parameters** are given in the form of _comma-separated list_ in _parentheses_, after the name. Each of the parameters, **must be be followed** by a _colon_ and a _type annotation_.

Function **result type** is defined by the _colon_ and another _type annotation_, after the parentheses. Following the function's result type is an **equals** sign and pair of **curly braces** that contain the _body of the function_. 

In cases when the interpreter or compiler can infer the function result type, it does not have to be specified. However, if, for example, the function is recursive, the result type must be explicitly specified.

If a function consists of just one statement, the **curly braces** may be _left out_.
