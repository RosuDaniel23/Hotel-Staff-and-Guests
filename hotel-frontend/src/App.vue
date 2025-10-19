<template>
  <div class="min-h-screen flex flex-col">
    <Navbar />
    <main class="flex-1">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
        <RouterView />
      </div>
    </main>
    <footer class="text-xs text-gray-500 py-6 text-center">© {{ new Date().getFullYear() }} Hotel Admin</footer>
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/components/ui/Navbar.vue'
</script>
