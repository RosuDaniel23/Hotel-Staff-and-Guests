<template>
  <header class="bg-white border-b border-gray-200">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
      <div class="flex items-center gap-3">
        <div class="h-9 w-9 rounded-xl bg-black text-white grid place-items-center font-bold">H</div>
        <h1 class="text-lg font-semibold">Hotel Admin</h1>
      </div>
      <nav class="flex items-center gap-3 text-sm">
        <RouterLink class="px-3 py-2 rounded-xl hover:bg-gray-100" to="/">Dashboard</RouterLink>
        <RouterLink class="px-3 py-2 rounded-xl hover:bg-gray-100" to="/rooms">Rooms</RouterLink>
        <RouterLink class="px-3 py-2 rounded-xl hover:bg-gray-100" to="/employees">Employees</RouterLink>
        <RouterLink class="px-3 py-2 rounded-xl hover:bg-gray-100" to="/guests">Guests</RouterLink>
      </nav>
    </div>
  </header>
</template>

<style scoped>
a.router-link-active { background: #111827; color: #fff; }
</style>
