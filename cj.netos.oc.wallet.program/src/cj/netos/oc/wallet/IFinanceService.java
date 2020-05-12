package cj.netos.oc.wallet;

import cj.netos.oc.wallet.model.PayChannel;
import cj.netos.oc.wallet.model.WorkDay;
import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface IFinanceService {
    /**
     * 获取可用的会计日期<br>
     * - 如果最顶的会计日期在当日且为工作状态则直接返回当日；
     * - 如果为日切状态或会计日期不在当日，则插入当日的会计日期，标记为工作状态,并置前一日为日切状态，并返回
     *
     * @return
     * @throws CircuitException
     */
    String getActivingWorkday(String person) throws CircuitException;

    /**
     * 是否在日切中
     *
     * @return
     */
    boolean isSwitchingWorkDay();

    /**
     * 获取最顶的会计日期，即最近一天的
     *
     * @return
     */
    WorkDay getTopWorkday();

    void addPayChannel(PayChannel payChannel) throws CircuitException;

    void removePayChannel(String id) throws CircuitException;

    List<WorkDay> pageWorkday(int limit, int offset);
}
