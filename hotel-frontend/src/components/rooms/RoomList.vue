<template>
  <div class="card overflow-hidden">
    <table class="min-w-full text-left">
      <thead class="bg-gray-50 text-xs uppercase text-gray-500">
      <tr>
        <th class="p-3">Room</th>
        <th class="p-3">Type</th>
        <th class="p-3">Price</th>
        <th class="p-3">Status</th>
        <th class="p-3 text-right">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="r in items" :key="r.id" class="border-t hover:bg-gray-50/60">
        <td class="p-3 font-medium">{{ r.number }}</td>
        <td class="p-3">{{ r.type }}</td>
        <td class="p-3">€ {{ r.price }}</td>
        <td class="p-3"><StatusBadge :status="r.status" /></td>
        <td class="p-3 text-right space-x-2">
          <button class="btn btn-ghost" @click="$emit('edit', r)">Edit</button>
          <button class="btn btn-primary" @click="$emit('remove', r.id)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import type { Room } from '@/types'
import StatusBadge from '@/components/ui/StatusBadge.vue'
defineProps<{ items: Room[] }>()
defineEmits<{ (e:'edit', item:Room):void; (e:'remove', id:number):void }>()
</script>
