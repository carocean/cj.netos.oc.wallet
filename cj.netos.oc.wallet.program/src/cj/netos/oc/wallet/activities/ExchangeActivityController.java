package cj.netos.oc.wallet.activities;

import cj.netos.oc.wallet.IExchangeActivityController;
import cj.netos.oc.wallet.bo.ExchangeBO;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.orm.mybatis.annotation.CjTransaction;

@CjBridge(aspects = "@transaction")
@CjService(name = "exchangeActivityController")
public class ExchangeActivityController implements IExchangeActivityController {
    @CjTransaction
    @Override
    public void receipt(ExchangeBO bo) {

    }
}
