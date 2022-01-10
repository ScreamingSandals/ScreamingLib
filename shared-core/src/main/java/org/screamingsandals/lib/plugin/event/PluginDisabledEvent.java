/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.plugin.event;

import lombok.Data;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.plugin.PluginDescription;

/**
 * NOTE: Only Platforms that support plugin enabling and disabling of plugins have this event.
 */
@Data
public class PluginDisabledEvent implements SEvent {
    private final PluginDescription plugin;
}