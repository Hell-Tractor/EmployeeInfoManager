<script setup lang="ts">
import { Ref, ref } from 'vue';
import { rules } from '../../utils/validators';
import request from '../../utils/request';
import { RiskTag } from '../RiskTagTable.vue';
import { useAlertStore, useUserStore } from '../../utils/store';

export interface EmploymentVo {
  staffId: number;
  departId: number;
  project: string;
  validSince: Date;
  validUntil: Date;
  workPermit: string;
  riskTagIds: number[];
  violation: string;
}

const emits = defineEmits<{ success: [EmploymentVo] }>();
const props = defineProps<{ allRiskTags: RiskTag[] }>();

const staffPersonId: Ref<string> = ref('');
const project: Ref<string> = ref('');
const validSince: Ref<Date> = ref(new Date());
const validUntil: Ref<Date> = ref(new Date());
const workPermit: Ref<string> = ref('');
const riskTagIds: Ref<number[]> = ref([]);
const violation: Ref<string> = ref('');
const isValid: Ref<boolean> = ref(false);
const imageFile: Ref<File | null> = ref(null);
const imageURL: Ref<string> = ref('');

function reset() {
    staffPersonId.value = '';
    project.value = '';
    validSince.value = new Date();
    validUntil.value = new Date();
    workPermit.value = '';
    riskTagIds.value = [];
    violation.value = '';
    imageFile.value = null;
    imageURL.value = '';
}

const saving: Ref<boolean> = ref(false);

function replaceNullByEmptyString(employment: EmploymentVo) {
    if (employment.violation === null) {
        employment.violation = '';
    }
}

async function save() {
    saving.value = true;
    let staffId: number | undefined = undefined;
    try {
        let response = await request.get('staff', { params: { personId: staffPersonId.value } });
        staffId = response.data.data.id;
    } catch (error) {
        console.error(error);
        saving.value = false;
        return;
    }
    try {
        await uploadImage();

        let employment: EmploymentVo = {
            staffId: staffId!,
            departId: useUserStore().departId,
            project: project.value,
            validSince: validSince.value,
            validUntil: validUntil.value,
            workPermit: workPermit.value,
            riskTagIds: riskTagIds.value,
            violation: violation.value
        };
        replaceNullByEmptyString(employment);
        await request.put('employment', employment);
        useAlertStore().showMessage('success', '保存成功');
        emits('success', employment);
        reset();
    } catch (error) {
        console.error(error);
        await deleteImage();
    } finally {
        saving.value = false;
    }
}

async function deleteImage() {
    if (!!workPermit.value) {
        try {
            await request.delete(`image/workPermit/${workPermit.value}`);
            workPermit.value = '';
        } catch (error) {
            console.error(error);
        }
    } else {
        console.log('No image to delete');
    }
}

async function uploadImage() {
    if (imageFile.value === null) {
        return;
    }
    try {
        await deleteImage();

        let formData = new FormData();
        formData.append('file', imageFile.value);
        let response = await request.put('image/upload/workPermit', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        workPermit.value = response.data.data;
    } catch (error) {
        console.error(error);
    }
}

async function selectImage() {
    let file = await new Promise<File>((resolve) => {
        let input = document.createElement('input');
        input.type = 'file';
        input.accept = 'image/*';
        input.onchange = () => {
            if (input.files && input.files.length > 0) {
                resolve(input.files[0]);
            }
        }
        input.click();
    });
    imageURL.value = URL.createObjectURL(file);
    imageFile.value = file;
}


</script>

<template>
    <v-dialog width="800px">
        <template v-slot:activator="{ props }">
            <slot name="activator" :props="props"></slot>
        </template>
        <template v-slot:default="{ isActive }">
            <v-form v-model="isValid">
                <v-card>
                    <v-card-text>
                        <v-text-field v-model="staffPersonId" label="员工身份证号" :rules="[rules.required, rules.personId]"></v-text-field>
                        <v-text-field v-model="project" label="项目名称" :rules="[rules.required, rules.projectName]"></v-text-field>
                        <v-row>
                            <v-col cols="6">
                                <v-text-field v-model="validSince" label="有效期自" type="date" :rules="[rules.required]"></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field v-model="validUntil" label="有效期至" type="date" :rules="[rules.required]"></v-text-field>
                            </v-col>
                        </v-row>
                        <v-img v-if="!!imageURL" :src="imageURL" max-height="200px"></v-img>
                        <v-btn v-else height="200px" width="100%" @click="selectImage()">上传工作许可照片</v-btn>
                        <v-select v-model="riskTagIds" :items="props.allRiskTags" item-title="name" item-value="id" label="风险标签" multiple chips></v-select>
                        <v-text-field v-model="violation" label="违规记录" :rules="[rules.maxLength128]"></v-text-field>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn @click="isActive.value = false">取消</v-btn>
                        <v-btn @click="isActive.value = false; save()" :disabled="isValid != true">保存</v-btn>
                    </v-card-actions>
                </v-card>
            </v-form>
        </template>
    </v-dialog>
</template>