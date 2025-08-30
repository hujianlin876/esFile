<template>
  <el-form
    ref="formRef"
    :model="model"
    :rules="rules"
    :label-width="labelWidth"
    :label-position="labelPosition"
    :size="size"
    :disabled="disabled"
    @submit.prevent="handleSubmit"
  >
    <slot />
    
    <el-form-item v-if="showSubmit" :label-width="submitLabelWidth">
      <el-button
        type="primary"
        :loading="loading"
        @click="handleSubmit"
      >
        {{ submitText }}
      </el-button>
      <el-button v-if="showReset" @click="handleReset">
        {{ resetText }}
      </el-button>
      <el-button v-if="showCancel" @click="handleCancel">
        {{ cancelText }}
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

interface Props {
  model: Record<string, any>
  rules?: FormRules
  labelWidth?: string | number
  labelPosition?: 'left' | 'right' | 'top'
  size?: 'large' | 'default' | 'small'
  disabled?: boolean
  showSubmit?: boolean
  showReset?: boolean
  showCancel?: boolean
  submitText?: string
  resetText?: string
  cancelText?: string
  submitLabelWidth?: string | number
  loading?: boolean
}

interface Emits {
  (e: 'submit', data: Record<string, any>): void
  (e: 'reset'): void
  (e: 'cancel'): void
}

const props = withDefaults(defineProps<Props>(), {
  labelWidth: 'auto',
  labelPosition: 'right',
  size: 'default',
  disabled: false,
  showSubmit: true,
  showReset: false,
  showCancel: false,
  submitText: '提交',
  resetText: '重置',
  cancelText: '取消',
  submitLabelWidth: 'auto',
  loading: false
})

const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()

// 计算属性
const submitLabelWidth = computed(() => {
  if (props.submitLabelWidth !== 'auto') {
    return props.submitLabelWidth
  }
  return props.labelWidth === 'auto' ? 'auto' : 0
})

// 方法
const validate = async () => {
  if (!formRef.value) return false
  
  try {
    await formRef.value.validate()
    return true
  } catch (error) {
    return false
  }
}

const resetFields = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const clearValidate = (props?: string | string[]) => {
  if (formRef.value) {
    formRef.value.clearValidate(props)
  }
}

const handleSubmit = async () => {
  if (await validate()) {
    emit('submit', props.model)
  }
}

const handleReset = () => {
  resetFields()
  emit('reset')
}

const handleCancel = () => {
  emit('cancel')
}

// 暴露方法
defineExpose({
  validate,
  resetFields,
  clearValidate,
  formRef
})
</script>

<style scoped>
.el-form-item:last-child {
  margin-bottom: 0;
}
</style>
