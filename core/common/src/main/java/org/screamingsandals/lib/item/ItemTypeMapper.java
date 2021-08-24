package org.screamingsandals.lib.item;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

@AbstractService
public abstract class ItemTypeMapper extends AbstractTypeMapper<ItemTypeHolder> {

    protected BidirectionalConverter<ItemTypeHolder> itemTypeConverter = BidirectionalConverter.<ItemTypeHolder>build()
            .registerP2W(ItemTypeHolder.class, i -> i);

    private static ItemTypeMapper itemTypeMapper;

    protected ItemTypeMapper() {
        if (itemTypeMapper != null) {
            throw new UnsupportedOperationException("ItemTypeMapper is already initialized.");
        }

        itemTypeMapper = this;
    }

    @OnPostConstruct
    public void remap() {
        // TODO: legacy and another mappings
    }



}
