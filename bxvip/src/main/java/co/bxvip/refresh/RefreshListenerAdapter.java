package co.bxvip.refresh;

public abstract class RefreshListenerAdapter implements PullListener {

        @Override
        public void onRefresh(BxRefreshLayout refreshLayout) {
        }

        @Override
        public void onLoadMore(BxRefreshLayout refreshLayout) {
        }

        @Override
        public void onPullingDown(BxRefreshLayout refreshLayout, float fraction) {
        }

        @Override
        public void onPullingUp(BxRefreshLayout refreshLayout, float fraction) {
        }

        @Override
        public void onPullDownReleasing(BxRefreshLayout refreshLayout, float fraction) {
        }

        @Override
        public void onPullUpReleasing(BxRefreshLayout refreshLayout, float fraction) {
        }

        @Override
        public void onFinishRefresh() {

        }

        @Override
        public void onFinishLoadMore() {

        }

        @Override
        public void onRefreshCanceled() {

        }

        @Override
        public void onLoadMoreCanceled() {

        }
}