<template>
  <div class="space-y-4">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-2">
      <h2 class="text-xl font-bold">Guests</h2>
      <div class="flex items-center gap-2">
        <input
            v-model="q"
            placeholder="Search name, email, phone..."
            class="border rounded p-2 w-72"
        />
        <button class="px-3 py-2 bg-black text-white rounded-lg" @click="startCreate">
          Add Guest
        </button>
      </div>
    </div>

    <Card v-if="loading"><Loader/></Card>
    <Card v-else-if="error"><p class="text-red-600">{{ error }}</p></Card>
    <Card v-else-if="!filtered.length">No guests yet.</Card>

    <GuestList
        v-else
        :items="filtered"
        @edit="edit"
        @remove="removeItem"
    />

    <GuestForm
        v-if="editing"
        :model="current"
        @cancel="cancel"
        @save="save"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { listGuests, createGuest, updateGuest, deleteGuest } from '@/api/guests'
import type { Guest } from '@/types'
import GuestList from '@/components/guests/GuestList.vue'
import GuestForm from '@/components/guests/GuestForm.vue'
import Card from '@/components/ui/Card.vue'
import Loader from '@/components/ui/Loader.vue'

const guests = ref<Guest[]>([])
const loading = ref(false)
const error = ref<string|null>(null)

const editing = ref(false)
const current = ref<Guest | null>(null)

const q = ref('')
const filtered = computed(() => {
  const s = q.value.toLowerCase().trim()
  if (!s) return guests.value
  return guests.value.filter(g =>
      [g.name, g.email, g.phone].join(' ').toLowerCase().includes(s)
  )
})

onMounted(load)

async function load() {
  loading.value = true; error.value = null
  try {
    const { data } = await listGuests()
    guests.value = data
  } catch (e:any) {
    error.value = e?.message ?? 'Failed to load guests'
  } finally { loading.value = false }
}

function startCreate() {
  current.value = { name: '', email: '', phone: '' }
  editing.value = true
}

function edit(item: Guest) {
  current.value = { ...item }
  editing.value = true
}

async function save(item: Guest) {
  if (item.id) await updateGuest(item.id, item)
  else await createGuest(item)
  editing.value = false
  await load()
}

async function removeItem(id: number) {
  if (!confirm('Delete guest?')) return
  await deleteGuest(id)
  await load()
}

function cancel() { editing.value = false }
</script>
