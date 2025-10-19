<template>
  <section class="grid sm:grid-cols-2 lg:grid-cols-3 gap-5">
    <Card>
      <p class="text-sm text-gray-500">Total Rooms</p>
      <p class="text-3xl font-semibold mt-1">{{ totals.total }}</p>
    </Card>
    <Card>
      <p class="text-sm text-gray-500">Available</p>
      <p class="text-3xl font-semibold mt-1">{{ totals.available }}</p>
    </Card>
    <Card>
      <p class="text-sm text-gray-500">Booked</p>
      <p class="text-3xl font-semibold mt-1">{{ totals.booked }}</p>
    </Card>
  </section>

  <section class="grid lg:grid-cols-2 gap-5">
    <Card>
      <div class="flex items-center justify-between mb-3">
        <h3 class="text-lg font-semibold">Available now</h3>
        <RouterLink class="btn btn-ghost" to="/rooms">View all</RouterLink>
      </div>
      <div v-if="loading"><Loader/></div>
      <ul v-else class="divide-y divide-gray-100">
        <li v-for="r in available.slice(0,8)" :key="r.id" class="py-3 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <span class="h-9 w-9 rounded-xl bg-gray-900 text-white grid place-items-center text-sm font-semibold">{{ r.number }}</span>
            <div>
              <p class="font-medium">{{ r.type }}</p>
              <p class="text-xs text-gray-500">€ {{ r.price }}</p>
            </div>
          </div>
          <StatusBadge status="AVAILABLE" />
        </li>
        <li v-if="available.length===0" class="text-sm text-gray-500">No rooms available.</li>
      </ul>
    </Card>

    <Card>
      <div class="flex items-center justify-between mb-3">
        <h3 class="text-lg font-semibold">Currently booked</h3>
        <RouterLink class="btn btn-ghost" to="/rooms">View all</RouterLink>
      </div>
      <div v-if="loading"><Loader/></div>
      <ul v-else class="divide-y divide-gray-100">
        <li v-for="r in booked.slice(0,8)" :key="r.id" class="py-3 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <span class="h-9 w-9 rounded-xl bg-gray-900 text-white grid place-items-center text-sm font-semibold">{{ r.number }}</span>
            <div>
              <p class="font-medium">{{ r.type }}</p>
              <p class="text-xs text-gray-500">€ {{ r.price }}</p>
            </div>
          </div>
          <StatusBadge status="BOOKED" />
        </li>
        <li v-if="booked.length===0" class="text-sm text-gray-500">No rooms booked.</li>
      </ul>
    </Card>
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import Card from '@/components/ui/Card.vue'
import Loader from '@/components/ui/Loader.vue'
import StatusBadge from '@/components/ui/StatusBadge.vue'
import { listRooms } from '@/api/rooms'
import type { Room } from '@/types'

const rooms = ref<Room[]>([])
const loading = ref(false)

const available = computed(() => rooms.value.filter(r => r.status === 'AVAILABLE'))
const booked    = computed(() => rooms.value.filter(r => r.status === 'BOOKED'))
const totals = computed(() => ({
  total: rooms.value.length,
  available: available.value.length,
  booked: booked.value.length
}))

onMounted(load)
async function load() {
  loading.value = true
  try {
    const { data } = await listRooms()
    rooms.value = data
  } catch {
    // fallback dacă încă nu ai endpoint /rooms:
    rooms.value = [
      { id: 1, number: '101', type: 'STANDARD', price: 90, status: 'AVAILABLE' },
      { id: 2, number: '102', type: 'DELUXE',   price: 120, status: 'BOOKED' },
      { id: 3, number: '103', type: 'SUITE',    price: 180, status: 'AVAILABLE' },
      { id: 4, number: '104', type: 'STANDARD', price: 95, status: 'BOOKED' }
    ] as Room[]
  } finally { loading.value = false }
}
</script>
