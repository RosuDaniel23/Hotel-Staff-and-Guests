<template>
  <div class="bg-white rounded-xl shadow p-4">
    <h3 class="font-semibold mb-2">{{ model?.id ? 'Edit Room' : 'New Room' }}</h3>
    <form class="grid gap-3" @submit.prevent="onSave">
      <input v-model="local.number" placeholder="Number" class="border p-2 rounded" required />
      <input v-model.number="local.price" placeholder="Price" type="number" class="border p-2 rounded" required />
      <select v-model="local.type" class="border p-2 rounded">
        <option>STANDARD</option><option>DELUXE</option><option>SUITE</option>
      </select>
      <select v-model="local.status" class="border p-2 rounded">
        <option>AVAILABLE</option><option>BOOKED</option><option>MAINTENANCE</option>
      </select>
      <div class="flex gap-2">
        <button class="px-3 py-2 bg-green-700 text-white rounded" type="submit">Save</button>
        <button class="px-3 py-2" type="button" @click="$emit('cancel')">Cancel</button>
      </div>
    </form>
  </div>
</template>
<script setup lang="ts">
import { reactive, watch } from 'vue'
import type { Room } from '@/types'
const props = defineProps<{ model: Room | null }>()
const emit = defineEmits<{ (e:'save', item:Room):void; (e:'cancel'):void }>()
const local = reactive<Room>({ number:'', type:'STANDARD', price:0, status:'AVAILABLE' })
watch(() => props.model, (m) => { if (m) Object.assign(local, m) }, { immediate:true })
function onSave(){ emit('save', { ...local }) }
</script>
