# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

### Common Build Commands
```
sbt compile           # compile all projects
sbt test              # compile and run all projects' tests
sbt fixall            # reformat and lint all source files
sbt fmtall            # format all source code
sbt quine/run         # to build and run Quine
sbt quine/assembly    # assemble Quine into a jar
```

### Testing Commands
```
# Run specific tests
sbt "quine/testOnly *SpecificTestName"

# Run integration tests (normally skipped)
sbt "quine/integration:test"
```

## Running Quine

### From SBT
```
sbt quine/run
```

### From JAR
```
java -jar quine-x.x.x.jar -p 8080
```

### With Docker
```
docker run -p 8080:8080 thatdot/quine
```

### Recipe-based execution
```
java -jar quine-x.x.x.jar -r wikipedia
```

## Project Architecture

Quine is a streaming graph interpreter that processes data in real-time, builds it into a graph structure, and runs live computation on that graph.

### Key Design Elements

1. **Graph-structured data model**
2. **Asynchronous actor-based graph computational model** (using Apache Pekko)
3. **Standing queries** (persistent queries that monitor the graph)

### Main Modules

- **quine-core**: Core streaming graph interpreter
- **quine-*-persistor**: Persistence implementations (RocksDB, MapDB, Cassandra)
- **quine-cypher**: Cypher query language support
- **quine-gremlin**: Gremlin traversal language support
- **quine-endpoints**: REST API specifications
- **quine-browser**: Web application interface
- **quine**: Main application integrating all components

### Technical Stack

- **Scala 2.13**: Primary programming language
- **Apache Pekko**: Actor system (fork of Akka)
- **ScalaJS**: JavaScript interop for browser components
- **Various persistence options**: RocksDB (default), MapDB, Cassandra

## Code Organization

### Actor Model

Quine is built around an actor-based computational model:
- Each node in the graph is represented by an actor
- Computation is distributed across these actors
- Messages are passed between actors to perform operations

### Persistors

Multiple persistence implementations are available:
- **RocksDB**: Default, embedded key-value store
- **MapDB**: Pure Java database
- **Cassandra**: Distributed database for larger deployments

### Data Flow

1. **Ingest Streams**: Read data from sources (Kafka, Kinesis, files, etc.)
2. **Graph Processing**: Data is processed into a graph structure
3. **Standing Queries**: Monitor the graph for specific patterns
4. **Actions**: Trigger behaviors when patterns are detected

## Recipes

Recipes are YAML documents that define:
- **Ingest Streams**: Data sources and how to process them
- **Standing Queries**: Patterns to watch for in the graph
- **UI Configuration**: Customization of the web interface

Example of running with a recipe:
```
java -jar quine-x.x.x.jar -r wikipedia
```

## Development Notes

- Quine requires **JDK 17** or newer
- Frontend components require **Yarn 0.22.0+**
- Use `sbt fixall` before committing to ensure code style compliance
- Tests are written with ScalaTest; integration tests are tagged separately
- Configuration is handled through Typesafe Config (reference.conf)

## Common Development Workflows

1. **Adding a new feature**:
   - Add code to appropriate module
   - Write tests
   - Run `sbt fixall` to format and lint
   - Test with `sbt test`

2. **Creating a new recipe**:
   - Create YAML file in `quine/recipes/`
   - Define ingest streams, standing queries, and UI config
   - Test with `java -jar quine.jar -r your-recipe.yaml`

3. **Debugging**:
   - Use SBT's `quine/run` for interactive development
   - Check logs (uses Logback)
   - Use the web UI (default: http://localhost:8080)

4. **Testing performance**:
   - Different persistors have different performance characteristics
   - RocksDB is typically the best for most workloads
   - Adjust shardCount in configuration for parallelism