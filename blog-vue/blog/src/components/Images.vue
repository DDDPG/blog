<template>
  <!--背景轮播-->
  <div id="imgs">
    <ul>
      <li
        class="item"
        style="
          background-image: url('https://tva3.sinaimg.cn/large/6833939bly1giclgi503lj20zk0m8hdt.jpg');
        "
      ></li>
      <li
        class="item"
        style="
          background-image: url('https://tva3.sinaimg.cn/large/6833939bly1gicli3sbvtj20zk0m8x6p.jpg');
        "
      ></li>
      <li
        class="item"
        style="
          background-image: url('https://tva3.sinaimg.cn/large/6833939bly1giclx29mstj20zk0m8hdt.jpg');
        "
      ></li>
      <li
        class="item"
        style="
          background-image: url('https://tva3.sinaimg.cn/large/6833939bly1giciszlczyj20zk0m816d.jpg');
        "
      ></li>
      <li
        class="item"
        style="
          background-image: url('https://tva3.sinaimg.cn/large/6833939bly1giciukx8a7j20zk0m8aio.jpg');
        "
      ></li>
      <li
        class="item"
        style="
          background-image: url('https://tva3.sinaimg.cn/large/6833939bly1giph4baakhj20zk0m8h5q.jpg');
        "
      ></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "Images",
};
</script>

<style lang="scss" scoped>
#imgs {
  display: block;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 70vh;
  min-height: 25rem;
  z-index: -9;
  background-color: #363636;
  .item {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    background-size: cover;
    background-position: 50% 50%;
    background-repeat: no-repeat;
    opacity: 0;
    z-index: 0;
    animation: imageAnimation 36s linear infinite 0s;
    backface-visibility: hidden;
    transform-style: preserve-3d;
    &:nth-child(2) {
      animation-delay: 6s;
    }
    &:nth-child(3) {
      animation-delay: 12s;
    }
    &:nth-child(4) {
      animation-delay: 18s;
    }
    &:nth-child(5) {
      animation-delay: 24s;
    }
    &:nth-child(6) {
      animation-delay: 30s;
    }
  }
}

@keyframes imageAnimation {
  0% {
    opacity: 0;
    animation-timing-function: ease-in;
  }

  2% {
    opacity: 1;
  }

  8% {
    opacity: 1;
    transform: scale(1.05);
    animation-timing-function: ease-out;
  }

  17% {
    opacity: 1;
    transform: scale(1.1);
  }

  25% {
    opacity: 0;
    transform: scale(1.1);
  }

  100% {
    opacity: 0;
  }
}
</style>
