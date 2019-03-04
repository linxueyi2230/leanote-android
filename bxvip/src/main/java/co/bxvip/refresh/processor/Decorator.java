package co.bxvip.refresh.processor;


import co.bxvip.refresh.BxRefreshLayout;

/**
 * Created by lcodecore on 2017/3/3.
 */

public abstract class Decorator implements IDecorator {
    protected IDecorator decorator;
    protected BxRefreshLayout.CoContext cp;

    public Decorator(BxRefreshLayout.CoContext processor, IDecorator decorator1) {
        cp = processor;
        decorator = decorator1;
    }
}
