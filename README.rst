TopMod
======

A REST interface to Topic Modeling.

Usage
-----

Edit ``config.yml`` as needed. Then compile and run TopMod
itself::

  mvn clean package &&
    ./target/appassembler/bin/topmod server config.yml

One demo endpoint exists::

  curl -H "Content-Type: application/json" \
    http://localhost:8080/suggest -d '["keys", "to", "look", "for"]'
