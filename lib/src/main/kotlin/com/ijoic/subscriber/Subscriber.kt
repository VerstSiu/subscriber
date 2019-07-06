/*
 *
 *  Copyright(c) 2019 VerstSiu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.ijoic.subscriber

/**
 * Subscriber
 *
 * @author verstsiu created at 2019-07-03 20:10
 */
class Subscriber<NODE, CHANNEL>(
  private val onChannelActive: (CHANNEL) -> Unit,
  private val onChannelInactive: (CHANNEL) -> Unit) {

  private val nodeInfoMap = mutableMapOf<NODE, Set<CHANNEL>>()
  private val channelMap = mutableMapOf<CHANNEL, Set<NODE>>()

  /**
   * Empty status
   */
  val isEmpty: Boolean
    get() = nodeInfoMap.isEmpty()

  /**
   * Subscribe [node] with [channel]
   */
  fun sub(node: NODE, channel: CHANNEL) {
    val oldChannelActive = isChannelActive(channel)
    nodeInfoMap.addItem(node, channel)
    channelMap.addItem(channel, node)

    if (!oldChannelActive) {
      onChannelActive.invoke(channel)
    }
  }

  /**
   * Unsubscribe [node] with [channel]
   */
  fun unsub(node: NODE, channel: CHANNEL) {
    val oldChannelActive = isChannelActive(channel)
    nodeInfoMap.removeItem(node, channel)
    channelMap.removeItem(channel, node)

    if (oldChannelActive && !isChannelActive(channel)) {
      onChannelInactive.invoke(channel)
    }
  }

  /**
   * Release [node]
   */
  fun release(node: NODE) {
    val oldChannels = nodeInfoMap[node]
    nodeInfoMap.remove(node)

    if (!oldChannels.isNullOrEmpty()) {
      oldChannels.forEach { channel ->
        if (channelMap.removeItem(channel, node) && !isChannelActive(channel)) {
          onChannelInactive.invoke(channel)
        }
      }
    }
  }

  /**
   * Returns active nodes of [channel]
   */
  fun getActiveNodes(channel: CHANNEL): Set<NODE>? {
    return channelMap[channel]
  }

  /**
   * Returns [channel] active status
   */
  private fun isChannelActive(channel: CHANNEL): Boolean {
    return !channelMap[channel].isNullOrEmpty()
  }

  /**
   * Add [item]
   */
  private fun <KEY, ITEM> MutableMap<KEY, Set<ITEM>>.addItem(key: KEY, item: ITEM): Boolean {
    val oldItems = this[key]

    if (oldItems.isNullOrEmpty()) {
      this[key] = setOf(item)
      return true
    }
    if (!oldItems.contains(item)) {
      this[key] = oldItems.plus(item)
      return true
    }
    return false
  }

  /**
   * Remove [item]
   */
  private fun <KEY, ITEM> MutableMap<KEY, Set<ITEM>>.removeItem(key: KEY, item: ITEM): Boolean {
    val oldItems = this[key]

    if (oldItems.isNullOrEmpty() || !oldItems.contains(item)) {
      return false
    }
    if (oldItems.size == 1) {
      this.remove(key)
    } else {
      this[key] = oldItems.minus(item)
    }
    return true
  }
}