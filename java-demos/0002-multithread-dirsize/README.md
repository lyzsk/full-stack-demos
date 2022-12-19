# 0002-multithread-dirsize

## dependency

```xml
		<!--	用来调FileUtils.sizeOfDirectory	-->
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.11.0</version>
		</dependency>
```

## 

重载 `run()` 方法

```java
        @Override
        public void run() {
            lock.lock();
            try {
                dirTotalSize.add(FileUtils.sizeOfDirectory(new File(dirPath)));
            } finally {
                latch.countDown();
                lock.unlock();
            }
        }
```