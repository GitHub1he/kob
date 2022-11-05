<template>
  <div class="flag-a flag" v-if="$store.state.pk.a_id === parseInt($store.state.user.id)">
    <img class="img-fluid img-sm" :src="$store.state.user.photo" alt="">
    &#45; &gt;
  </div>
  <div class="flag-b flag" v-if="$store.state.pk.b_id === parseInt($store.state.user.id)">
    &lt; &#45;
    <img class="img-fluid img-sm" :src="$store.state.user.photo" alt="">
  </div>
  <div ref="parent" class="gamemap">
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { GameMap } from "@/assets/script/GameMap";
import { ref, onMounted } from 'vue';
import { useStore } from "vuex";

export default {
  setup() {
    let parent = ref(null);
    let canvas = ref(null);
    const store = useStore();

    console.log(store.state.pk.a_id, store.state.user.id);
    onMounted(() => {
      store.commit(
        "updateGameObject",
        new GameMap(canvas.value.getContext('2d'), parent.value, store)
      )
    })
    return {
      parent,
      canvas,
    }
  }
}

</script>

<style scoped>
.gamemap {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.flag {
  width: 10vw;
  height: 10vh;
  position: absolute;
  
  background-color: rgba(50, 50, 50, 0.5);
}

img {
  border-radius: 50%;
  height: 6vw;
  width: 6vw;
}

.flag-a {
  background-color: #466f95;
  float: left;
  left: 10vw;
  top: 80vh;
}

.flag-b {
  background-color: #deaeb1;
  float: right;
  right: 10vw;
  top: 20vh;
}


</style>