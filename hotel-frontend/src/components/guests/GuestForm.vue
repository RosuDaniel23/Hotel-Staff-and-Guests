<template>
  <div class="bg-white rounded-2xl shadow p-4">
    <h3 class="font-semibold mb-2">{{ model?.id ? 'Edit Guest' : 'New Guest' }}</h3>
    <form class="grid gap-3" @submit.prevent="onSave">
      <div>
        <input v-model="local.name" placeholder="Name" class="border p-2 rounded w-full" />
        <p v-if="errors.name" class="text-red-600 text-sm mt-1">{{ errors.name }}</p>
      </div>
      <div>
        <input v-model="local.email" placeholder="Email" type="email" class="border p-2 rounded w-full" />
        <p v-if="errors.email" class="text-red-600 text-sm mt-1">{{ errors.email }}</p>
      </div>
      <input v-model="local.phone" placeholder="Phone" class="border p-2 rounded w-full" />
      <div class="flex gap-2">
        <button class="px-3 py-2 bg-green-700 text-white rounded" type="submit">Save</button>
        <button class="px-3 py-2" type="button" @click="$emit('cancel')">Cancel</button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch, ref } from 'vue'
import type { Guest } from '@/types'

const props = defineProps<{ model: Guest | null }>()
const emit = defineEmits<{ (e:'save', item:Guest):void; (e:'cancel'):void }>()

const local = reactive<Guest>({ name: '', email: '', phone: '' })
const errors = ref<{ name?: string; email?: string }>({})

watch(() => props.model, (m) => { if (m) Object.assign(local, m) }, { immediate: true })

function onSave() {
  errors.value = {}
  if (!local.name?.trim()) errors.value.name = 'Name is required'
  if (!/^[^@]+@[^@]+\.[^@]+$/.test(local.email)) errors.value.email = 'Invalid email'
  if (Object.keys(errors.value).length) return
  emit('save', { ...local })
}
</script>
