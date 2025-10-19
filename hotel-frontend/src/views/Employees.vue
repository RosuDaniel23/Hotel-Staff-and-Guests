<template>
  <div class="space-y-4">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-2">
      <h2 class="text-xl font-bold">Employees</h2>
      <div class="flex items-center gap-2">
        <input
            v-model="q"
            placeholder="Search name, email, role..."
            class="border rounded p-2 w-72"
        />
        <button class="px-3 py-2 bg-black text-white rounded-lg" @click="startCreate">
          Add Employee
        </button>
      </div>
    </div>

    <Card v-if="loading"><Loader/></Card>
    <Card v-else-if="error"><p class="text-red-600">{{ error }}</p></Card>
    <Card v-else-if="!filtered.length">No employees yet.</Card>

    <EmployeeList
        v-else
        :items="filtered"
        @edit="edit"
        @remove="removeItem"
    />

    <EmployeeForm
        v-if="editing"
        :model="current"
        @cancel="cancel"
        @save="save"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { listEmployees, createEmployee, updateEmployee, deleteEmployee } from '@/api/employees'
import type { Employee } from '@/types'
import EmployeeList from '@/components/employees/EmployeeList.vue'
import EmployeeForm from '@/components/employees/EmployeeForm.vue'
import Card from '@/components/ui/Card.vue'
import Loader from '@/components/ui/Loader.vue'

const employees = ref<Employee[]>([])
const loading = ref(false)
const error = ref<string|null>(null)

const editing = ref(false)
const current = ref<Employee | null>(null)

const q = ref('')
const filtered = computed(() => {
  const s = q.value.toLowerCase().trim()
  if (!s) return employees.value
  return employees.value.filter(e =>
      [e.name, e.email, e.role].join(' ').toLowerCase().includes(s)
  )
})

onMounted(load)

async function load() {
  loading.value = true; error.value = null
  try {
    const { data } = await listEmployees()
    employees.value = data
  } catch (e: any) {
    error.value = e?.message ?? 'Failed to load employees'
  } finally { loading.value = false }
}

function startCreate() {
  current.value = { name: '', role: 'OTHER', email: '' }
  editing.value = true
}

function edit(item: Employee) {
  current.value = { ...item }
  editing.value = true
}

async function save(item: Employee) {
  try {
    if (item.id) await updateEmployee(item.id, item)
    else await createEmployee(item)
    editing.value = false
    await load()
  } catch (e) {
    console.error(e)
  }
}

async function removeItem(id: number) {
  if (!confirm('Delete employee?')) return
  await deleteEmployee(id)
  await load()
}

function cancel() { editing.value = false }
</script>
