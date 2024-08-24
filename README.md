[![Publish Build to Github Releases](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-release.yaml/badge.svg)](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-release.yaml)
[![Publish package to GitHub Packages](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-package.yaml/badge.svg)](https://github.com/DynAPI/org.dynapi.squirtle/actions/workflows/publish-package.yaml)
# Squirtle - Java SQL Query Builder

```java
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;

public class Example {
    public static void main(String[] args) {
        Table table = new Schema("dynapi").table("test").as("t");
        String sql = new Query()
                .from(table)
                .select(table.asStar())
                .getSql();
        System.out.println(sql);  // SELECT "t".* FROM "dynapi"."test" "t"
    }
}
```
