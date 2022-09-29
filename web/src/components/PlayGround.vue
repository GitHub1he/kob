<template>
    <div class="playground">
      <GameMap>
        <slot></slot>
      </GameMap>
    </div>
  
</template>

<script>
import GameMap from '@/components/GameMap.vue';
export default { 
  components: {
    GameMap,
}
}
</script>

<style scoped>
.playground{
  margin-top: 5vh;
  height: 80vh;
}
</style>