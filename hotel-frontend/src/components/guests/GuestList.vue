<template>
  <div class="bg-white rounded-2xl shadow overflow-x-auto">
    <table class="min-w-full text-left">
      <thead class="bg-gray-100">
      <tr>
        <th class="p-3 cursor-pointer" @click="sortBy('name')">
          Name <small v-if="sortKey==='name'">▲</small>
        </th>
        <th class="p-3 cursor-pointer" @click="sortBy('email')">
          Email <small v-if="sortKey==='email'">▲</small>
        </th>
        <th class="p-3 cursor-pointer" @click="sortBy('phone')">
          Phone <small v-if="sortKey==='phone'">▲</small>
        </th>
        <th class="p-3 text-right">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="g in sorted" :key="g.id" class="border-t">
        <td class="p-3">{{ g.name }}</td>
        <td class="p-3">{{ g.email }}</td>
        <td class="p-3">{{ g.phone }}</td>
        <td class="p-3 text-right space-x-2">
          <button class="px-2 py-1 bg-gray-800 text-white rounded" @click="$emit('edit', g)">Edit</button>
          <button class="px-2 py-1 bg-red-600 text-white rounded" @click="$emit('remove', g.id)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Guest } from '@/types'

const props = defineProps<{ items: Guest[] }>()
defineEmits<{ (e:'edit', item:Guest):void; (e:'remove', id:number):void }>()

const sortKey = ref<'name'|'email'|'phone'>('name')
const sorted = computed(() =>
    [...props.items].sort((a,b) =>
        String(a[sortKey.value] ?? '').localeCompare(String(b[sortKey.value] ?? ''))
    )
)
function sortBy(k:'name'|'email'|'phone'){ sortKey.value = k }
</script>
