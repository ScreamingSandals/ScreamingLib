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

package org.screamingsandals.lib.block.state;

import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.container.ContainerHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;

// TODO: Metadata
public interface BlockStateHolder extends Wrapper, ContainerHolder {

    BlockTypeHolder getType();

    void setType(BlockTypeHolder type);

    LocationHolder getLocation();

    default BlockHolder getBlock() {
        return BlockMapper.wrapBlock(getLocation());
    }

    default boolean updateBlock() {
        return updateBlock(false);
    }

    default boolean updateBlock(boolean force) {
        return updateBlock(force, true);
    }

    boolean updateBlock(boolean force, boolean applyPhysics);
}
