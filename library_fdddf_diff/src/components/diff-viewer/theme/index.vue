<template>
    <diff-viewer />
</template>
<script>
import createStoriesPreview from '@lcap/builder/input/vue2/stories-preview';
import * as stories from '../stories/block.stories';

const DiffPreview = createStoriesPreview(stories);

export default {
components: {
    DiffPreview,
},
};

</script>