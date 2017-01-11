

###Build

`sbt clean package`

### Run

```bash
$ spark-submit --master {URI} \ 
  --jars {path/to/tika-app-1.8.jar} \ 
  target/scala-2.x/spark-hdfs-tika*.jar \ 
  {HDFS_OUTPUT_DIRECTORY} \ 
  {INPUT_FILE}
```

__getting input list of files from hdfs__

```bash
$ hadoop fs -ls -R /tmp/data | sed '/^d/ d' | awk '{print substr($0, index($0, $8))}' > input_file.txt
```

