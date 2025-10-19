<template>
  <div class="space-y-6">
    <!-- Header + acțiuni -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-3">
      <h2 class="text-xl font-semibold">Rooms</h2>
      <div class="flex items-center gap-2">
        <input
            v-model="q"
            placeholder="Search number / type / status..."
            class="input w-72"
        />
        <button class="btn btn-primary" @click="startCreate">Add Room</button>
      </div>
    </div>

    <!-- Stări -->
    <Card v-if="loading"><Loader/></Card>
    <Card v-else-if="error"><p class="text-red-600">{{ error }}</p></Card>
    <Card v-else-if="!filtered.length">No rooms found.</Card>

    <!-- Listă -->
    <RoomList
        v-else
        :items="filtered"
        @edit="edit"
        @remove="removeItem"
    />

    <!-- Formular -->
    <RoomForm
        v-if="editing"
        :model="current"
        @cancel="cancel"
        @save="save"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { listRooms, createRoom, updateRoom, deleteRoom } from '@/api/rooms'
import type { Room } from '@/types'
import RoomList from '@/components/rooms/RoomList.vue'
import RoomForm from '@/components/rooms/RoomForm.vue'
import Card from '@/components/ui/Card.vue'
import Loader from '@/components/ui/Loader.vue'

const rooms = ref<Room[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

const editing = ref(false)
const current = ref<Room | null>(null)

const q = ref('')
const filtered = computed(() => {
  const s = q.value.toLowerCase().trim()
  if (!s) return rooms.value
  return rooms.value.filter(r =>
      [r.number, r.type, r.status, r.price].join(' ').toLowerCase().includes(s)
  )
})

onMounted(load)

async function load() {
  loading.value = true; error.value = null
  try {
    const { data } = await listRooms()
    rooms.value = data
  } catch (e) {
    // fallback mock ca să vezi UI-ul dacă backendul nu e gata
    rooms.value = [
      { id: 1, number: '101', type: 'STANDARD', price: 90, status: 'AVAILABLE' },
      { id: 2, number: '102', type: 'DELUXE',   price: 120, status: 'BOOKED' },
      { id: 3, number: '103', type: 'SUITE',    price: 180, status: 'AVAILABLE' },
    ] as Room[]
  } finally { loading.value = false }
}

function startCreate() {
  current.value = { number: '', type: 'STANDARD', price: 0, status: 'AVAILABLE' }
  editing.value = true
}

function edit(item: Room) {
  current.value = { ...item }
  editing.value = true
}

async function save(item: Room) {
  if (item.id) await updateRoom(item.id, item)
  else await createRoom(item)
  editing.value = false
  await load()
}

async function removeItem(id: number) {
  if (!confirm('Delete room?')) return
  await deleteRoom(id)
  await load()
}

function cancel() { editing.value = false }
</script>
