package akka.params;

import akka.actor.ActorRef;

import java.util.Iterator;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 * 任务结果统一返回处理  需实现该接口自定义返回内容
 * <p>
 * 任务分割策略  需自己实现  返回对象为消息对象  汇总使用   需要序列化
 */
public interface AskHandle<S, R> {

    /**
     * 任务结果统一返回处理  需实现该接口自定义返回内容
     *
     * @param it
     * @return
     */
    R getReturn(final Iterator<Object> it);

    /**
     * 任务分割策略  需自己实现  返回对象为消息对象  汇总使用   需要序列化
     *
     * @param cutParam
     * @return
     */
    S cut(final CutParam<S> cutParam);

    /**
     * 单个任务成功返回
     *
     * @param actorRef
     * @param o
     */
    void onSuccess(ActorRef actorRef, Object o);

    /**
     * 任务异常失败
     *
     * @param actorRef
     * @param throwable
     * @param askHandle
     * @param cutParam
     */
    void onFailure(ActorRef actorRef, Throwable throwable, AskHandle<S, R> askHandle, CutParam cutParam);

    /**
     * 推送完结
     *
     * @param actorRef
     * @param throwable
     * @param o
     */
    void onComplete(ActorRef actorRef, Throwable throwable, Object o);

}
