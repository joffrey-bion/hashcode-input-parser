package org.hildan.hashcode.utils.parser.readers.creators;

import org.hildan.hashcode.utils.parser.context.Context;

public interface Int7Creator<T> extends ObjectCreator<T> {

    @Override
    default T create(Context context) {
        return create(context.readInt(), context.readInt(), context.readInt(), context.readInt(), context.readInt(),
                context.readInt(), context.readInt());
    }

    T create(int arg0, int arg1, int arg2, int arg3, int arg5, int arg6, int arg7);
}