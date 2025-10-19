<template>
  <div class="bg-white rounded-2xl shadow overflow-x-auto">
    <table class="min-w-full text-left">
      <thead class="bg-gray-100">
      <tr>
        <th class="p-3 cursor-pointer" @click="sortBy('name')">
          Name <small v-if="sortKey==='name'">▲</small>
        </th>
        <th class="p-3 cursor-pointer" @click="sortBy('role')">
          Role <small v-if="sortKey==='role'">▲</small>
        </th>
        <th class="p-3 cursor-pointer" @click="sortBy('email')">
          Email <small v-if="sortKey==='email'">▲</small>
        </th>
        <th class="p-3 text-right">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="e in sorted" :key="e.id" class="border-t">
        <td class="p-3">{{ e.name }}</td>
        <td class="p-3">{{ e.role }}</td>
        <td class="p-3">{{ e.email }}</td>
        <td class="p-3 text-right space-x-2">
          <button class="px-2 py-1 bg-gray-800 text-white rounded" @click="$emit('edit', e)">Edit</button>
          <button class="px-2 py-1 bg-red-600 text-white rounded" @click="$emit('remove', e.id)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Employee } from '@/types'

const props = defineProps<{ items: Employee[] }>()
defineEmits<{ (e: 'edit', item: Employee): void; (e: 'remove', id: number): void }>()

const sortKey = ref<'name'|'role'|'email'>('name')
const sorted = computed(() =>
    [...props.items].sort((a, b) =>
        String(a[sortKey.value] ?? '').localeCompare(String(b[sortKey.value] ?? ''))
    )
)
function sortBy(k: 'name'|'role'|'email'){ sortKey.value = k }
</script>
