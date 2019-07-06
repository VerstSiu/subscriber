
# Subscriber

[![](https://jitpack.io/v/VerstSiu/subscriber.svg)](https://jitpack.io/#VerstSiu/subscriber)

## Get Start

* Add it in your root build.gradle at the end of repositories:

    ```gradle
    allprojects {
        repositories {
          ...
          maven { url 'https://jitpack.io' }
        }
    }
    ```

* Add the dependency:

    ```gradle
    dependencies {
        implementation 'com.github.VerstSiu:subscriber:1.0'
    }
    ```

## Usage

1. Subscribe/unsubscribe session/node with channel info:

    ```kotlin
    val subManager = Subscriber<NODE, CHANNEL>(
      onChannelActive = { channel -> TODO() },
      onChannelInactive = { channel -> TODO() }
    )

    subManager.sub(node, channel)
    subManager.unsub(node, channel)
    ``` 

2. Get active nodes of channel to send message:

    ```kotlin
    val nodes = subManager.getActiveNodes(channel)

    nodes.forEach { it.send(message) }
    ```

3. Release node when session finished:

    ```kotlin
    // This will clear all related channels of node
    subManager.release(node)
    ```

## License

```

   Copyright(c) 2019 VerstSiu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```
