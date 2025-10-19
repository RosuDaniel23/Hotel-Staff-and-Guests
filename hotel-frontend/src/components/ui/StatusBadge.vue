<template>
  <span :class="klass">{{ text }}</span>
</template>

<script setup lang="ts">
const props = defineProps<{ status: 'AVAILABLE'|'BOOKED'|'MAINTENANCE' }>()
const map = {
  AVAILABLE: 'bg-green-100 text-green-800',
  BOOKED: 'bg-red-100 text-red-800',
  MAINTENANCE: 'bg-yellow-100 text-yellow-900',
} as const
const text = props.status
const klass = `pill ${map[props.status]}`
</script>
