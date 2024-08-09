[![Publish Build to Github Releases](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-release.yaml/badge.svg)](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-release.yaml)
[![Publish package to GitHub Packages](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-package.yaml/badge.svg)](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-package.yaml)
# Squirtle - Java SQL Query Builder

```java
package org.dynapi;

import org.dynapi.squirtle.core.queries.Database;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.Table;

public class Testing {
    public static void main(String[] args) {
        Table table = new Database("dynapi").getSchema("dynapi").getTable("test");
        String sql = Query
                .from(table)
                .select("*")
                .getSql();
        System.out.println(sql);  // SELECT * FROM "dynapi"."dynapi"."test"
    }
}
```
